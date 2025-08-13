package com.example.place.domain.keyword.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.example.place.common.annotation.Loggable;
import com.example.place.domain.keyword.service.dto.KeywordRankingDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchKeywordService {

	private final RedisTemplate<String, String> redisTemplate;
	private static final String KEYWORD_ZSET = "keyword_rankings";
	private static final DateTimeFormatter KEY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");

	private String getCurrentHourKey() {
		return KEYWORD_ZSET + ":" + LocalDateTime.now().format(KEY_FORMATTER);
	}

	/**
	 * 키워드 추가
	 */
	public void addKeyword(String keyword) {
		if (keyword == null || keyword.trim().isEmpty()) {
			log.warn("빈 키워드 무시됨");
			return;
		}

		String normalizedKeyword = keyword.toLowerCase().trim();
		if (normalizedKeyword.length() > 100) {
			log.warn("키워드 길이 초과: {}", normalizedKeyword.substring(0, 20) + "...");
			return;
		}

		try {
			String redisKey = getCurrentHourKey();

			// 점수 증가
			redisTemplate.opsForZSet().incrementScore(redisKey, normalizedKeyword, 1);

			// TTL이 없거나 (-1)일 때만 설정 (27시간)
			Long ttl = redisTemplate.getExpire(redisKey);
			if (ttl == null || ttl < 0) {
				redisTemplate.expire(redisKey, Duration.ofHours(25));
			}

			log.info("Redis 시간별 ZSet 점수 증가: {}, 키: {}", normalizedKeyword, redisKey);
		} catch (Exception e) {
			log.error("Redis 키워드 점수 증가 실패: {}", normalizedKeyword, e);
		}
	}

	/**
	 * 24시간 키워드 조회
	 * 지금 점수 - 24시간 전 점수 = 실제 24시간 검색량 증가분 계산
	 */
	@Loggable
	public List<KeywordRankingDto> getTopKeywordsLast24Hours(int limit) {
		try {
			LocalDateTime now = LocalDateTime.now();
			String nowKey = KEYWORD_ZSET + ":" + now.format(KEY_FORMATTER);
			String ago24Key = KEYWORD_ZSET + ":" + now.minusHours(24).format(KEY_FORMATTER);

			// 현재 시간의 키워드와 점수 조회
			Set<ZSetOperations.TypedTuple<String>> nowTuples =
				redisTemplate.opsForZSet().rangeWithScores(nowKey, 0, -1);

			// 24시간 전의 키워드와 점수 조회
			Set<ZSetOperations.TypedTuple<String>> agoTuples =
				redisTemplate.opsForZSet().rangeWithScores(ago24Key, 0, -1);

			Map<String, Double> scoreMap = new HashMap<>();

			// 현재 점수들을 맵에 저장
			if (nowTuples != null) {
				for (ZSetOperations.TypedTuple<String> tuple : nowTuples) {
					if (tuple != null && tuple.getValue() != null && tuple.getScore() != null) {
						scoreMap.put(tuple.getValue(), tuple.getScore());
					}
				}
			}

			// 24시간 전 점수 차감 (현재 점수에서 과거 점수를 뺌)
			if (agoTuples != null) {
				for (ZSetOperations.TypedTuple<String> tuple : agoTuples) {
					if (tuple != null && tuple.getValue() != null && tuple.getScore() != null) {
						// 이미 존재하는 값에서 24시간 전 점수를 빼기
						scoreMap.merge(tuple.getValue(), -tuple.getScore(), Double::sum);
					}
				}
			}

			// 점수 0 이하이거나 null인 키워드는 제거
			scoreMap.entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue() <= 0.0);

			// 점수 내림차순 정렬 후 limit 개수만큼 DTO 변환
			return scoreMap.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
				.limit(limit)
				.map(entry -> new KeywordRankingDto(entry.getKey(), entry.getValue().longValue()))
				.collect(Collectors.toList());

		} catch (Exception e) {
			log.error("24시간 키워드 조회 실패", e);
			return Collections.emptyList();
		}
	}
}