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
				redisTemplate.expire(redisKey, Duration.ofHours(27));
			}

			log.info("Redis 시간별 ZSet 점수 증가: {}, 키: {}", normalizedKeyword, redisKey);
		} catch (Exception e) {
			log.error("Redis 키워드 점수 증가 실패: {}", normalizedKeyword, e);
		}
	}

	/**
	 * 24시간 키워드 조회
	 * 지금 점수 - 24시간 전 점수 = 실제 24시간 검색량
	 */
	@Loggable
	public List<KeywordRankingDto> getTopKeywordsLast24Hours(int limit) {

		try {
			LocalDateTime now = LocalDateTime.now();
			String nowKey = KEYWORD_ZSET + ":" + now.format(KEY_FORMATTER);
			String ago24Key = KEYWORD_ZSET + ":" + now.minusHours(24).format(KEY_FORMATTER);

			// 현재 시간 키워드 + 점수
			Set<ZSetOperations.TypedTuple<String>> nowTuples =
				redisTemplate.opsForZSet().rangeWithScores(nowKey, 0, -1);

			// 24시간 전 키워드 + 점수
			Set<ZSetOperations.TypedTuple<String>> agoTuples =
				redisTemplate.opsForZSet().rangeWithScores(ago24Key, 0, -1);

			// 현재 점수 맵
			Map<String, Double> scoreMap = new HashMap<>();
			if (nowTuples != null) {
				for (ZSetOperations.TypedTuple<String> t : nowTuples) {
					if (t.getValue() != null && t.getScore() != null) {
						scoreMap.put(t.getValue(), t.getScore());
					}
				}
			}

			// 24시간 전 점수 차감
			if (agoTuples != null) {
				for (ZSetOperations.TypedTuple<String> t : agoTuples) {
					if (t.getValue() != null && t.getScore() != null) {
						scoreMap.merge(t.getValue(), -t.getScore(), Double::sum);
					}
				}
			}

			// 증가분이 0 이하인 항목 제거
			scoreMap.entrySet().removeIf(e -> e.getValue() == null || e.getValue() <= 0.0);

			// 정렬 후 DTO 변환
			return scoreMap.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
				.limit(limit)
				.map(e -> new KeywordRankingDto(e.getKey(), e.getValue().longValue()))
				.collect(Collectors.toList());

		} catch (Exception e) {
			log.error("24시간 키워드 조회 실패", e);
			return Collections.emptyList();
		}
	}
}