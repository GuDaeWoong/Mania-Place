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

			// TTL이 없거나 (-1)일 때만 설정 (25시간)
			Long ttl = redisTemplate.getExpire(redisKey);
			if (ttl < 0) {
				redisTemplate.expire(redisKey, Duration.ofHours(25));
			}

			log.info("Redis 시간별 ZSet 점수 증가: {}, 키: {}", normalizedKeyword, redisKey);
		} catch (Exception e) {
			log.error("Redis 키워드 점수 증가 실패: {}", normalizedKeyword, e);
		}
	}

	/**
	 * 인기 검색어 조회 (최근 24시간)
	 */
	@Loggable
	public List<KeywordRankingDto> getTopKeywordsLast24Hours(int limit) {
		try {
			// 1. 24시간 전부터 지금까지의 키들 만들기
			List<String> hourlyKeys = generateHourlyKeys();

			// 2. Redis에서 여러 ZSet 합산 후 상위 N개 바로 조회 (KEYS 명령어 없이)
			return getTopRankedKeywordsWithoutKeys(hourlyKeys, limit);
		} catch (Exception e) {
			log.error("인기 검색어 조회 실패", e);
			return Collections.emptyList();
		}
	}

	/**
	 * 24시간치 Redis 키 만들기
	 */
	private List<String> generateHourlyKeys() {
		List<String> keys = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();

		for (int hour = 23; hour >= 0; hour--) {
			LocalDateTime time = now.minusHours(hour);
			keys.add(KEYWORD_ZSET + ":" + time.format(KEY_FORMATTER));
		}

		return keys;
	}

	/**
	 * KEYS 명령어 없이 키워드 랭킹 조회 (수동 합산 방식)
	 */
	private List<KeywordRankingDto> getTopRankedKeywordsWithoutKeys(List<String> keys, int limit) {
		log.info("수동 합산 방식으로 키워드 랭킹 조회 시작 - 대상 키 개수: {}", keys.size());

		Map<String, Long> keywordScores = new HashMap<>();
		int validKeyCount = 0;

		for (String key : keys) {
			try {
				// 각 키에서 모든 데이터 조회 (키가 없으면 빈 Set 반환됨)
				Set<ZSetOperations.TypedTuple<String>> keyData =
					redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);

				if (keyData != null && !keyData.isEmpty()) {
					validKeyCount++;
					log.debug("키 {} 에서 {} 개 데이터 조회됨", key, keyData.size());

					for (ZSetOperations.TypedTuple<String> tuple : keyData) {
						if (tuple.getValue() != null && tuple.getScore() != null) {
							keywordScores.merge(tuple.getValue(),
								tuple.getScore().longValue(),
								Long::sum);
						}
					}
				} else {
					log.debug("키 {} 는 비어있음", key);
				}
			} catch (Exception e) {
				log.warn("키 {} 조회 실패: {}", key, e.getMessage());
			}
		}

		log.info("유효한 키 개수: {}, 총 키워드 개수: {}", validKeyCount, keywordScores.size());

		if (keywordScores.isEmpty()) {
			log.info("집계된 키워드가 없음 - 빈 결과 반환");
			return Collections.emptyList();
		}

		// 점수순으로 정렬 후 상위 N개 반환
		List<KeywordRankingDto> result = keywordScores.entrySet().stream()
			.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
			.limit(limit)
			.map(entry -> new KeywordRankingDto(entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());

		log.info("최종 랭킹 결과 개수: {}", result.size());
		return result;
	}

	/**
	 * Redis 결과를 DTO 리스트로 변환
	 */
	private List<KeywordRankingDto> convertToRankingList(Set<ZSetOperations.TypedTuple<String>> entries) {
		if (entries == null || entries.isEmpty()) {
			return Collections.emptyList();
		}

		return entries.stream()
			.filter(tuple -> tuple.getValue() != null && tuple.getScore() != null)
			.map(tuple -> new KeywordRankingDto(tuple.getValue(), tuple.getScore().longValue()))
			.collect(Collectors.toList());
	}
}