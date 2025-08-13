package com.example.place.domain.keyword.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
			if (ttl == null || ttl < 0) {
				redisTemplate.expire(redisKey, Duration.ofHours(25));
			}

			log.info("Redis 시간별 ZSet 점수 증가: {}, 키: {}", normalizedKeyword, redisKey);
		} catch (Exception e) {
			log.error("Redis 키워드 점수 증가 실패: {}", normalizedKeyword, e);
		}
	}

	/**
	 * 인기 검색어 조회
	 *
	 */
	@Loggable
	public List<KeywordRankingDto> getTopKeywordsLast24Hours(int limit) {
		try {
			// 1. 24시간 전부터 지금까지의 키들 만들기
			List<String> keys = make24HourKeys();

			// 2. 모든 키워드 점수 합치기
			Map<String, Double> scores = addUpAllScores(keys);

			// 3. 점수 높은 순으로 정렬해서 반환
			return getTopRanked(scores, limit);

		} catch (Exception e) {
			log.error("검색어 조회 실패", e);
			return Collections.emptyList();
		}
	}

	// 24시간치 Redis 키 만들기
	private List<String> make24HourKeys() {
		List<String> keys = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();

		// 23시간 전부터 지금까지
		for (int i = 23; i >= 0; i--) {
			LocalDateTime time = now.minusHours(i);
			String key = KEYWORD_ZSET + ":" + time.format(KEY_FORMATTER);
			keys.add(key);
		}

		return keys;
	}

	// 모든 키워드 점수 합치기
	private Map<String, Double> addUpAllScores(List<String> keys) {
		Map<String, Double> totalScores = new HashMap<>();

		for (String key : keys) {
			// 키가 Redis에 있는지 확인
			if (!redisTemplate.hasKey(key)) {
				continue;
			}

			// 해당 키의 모든 키워드와 점수 가져오기
			Set<ZSetOperations.TypedTuple<String>> data =
				redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);

			if (data == null) {
				continue;
			}

			// 각 키워드 점수를 합계에 더하기
			for (ZSetOperations.TypedTuple<String> item : data) {
				if (item != null && item.getValue() != null && item.getScore() != null) {
					String keyword = item.getValue();
					Double score = item.getScore();

					// 이미 있으면 더하고, 없으면 새로 추가
					if (totalScores.containsKey(keyword)) {
						totalScores.put(keyword, totalScores.get(keyword) + score);
					} else {
						totalScores.put(keyword, score);
					}
				}
			}
		}

		return totalScores;
	}

	// 점수 높은 순으로 상위 N개 뽑기
	private List<KeywordRankingDto> getTopRanked(Map<String, Double> scores, int limit) {
		List<KeywordRankingDto> result = new ArrayList<>();

		// 점수가 0보다 큰 것들만 리스트로 만들기
		List<Map.Entry<String, Double>> list = new ArrayList<>();
		for (Map.Entry<String, Double> entry : scores.entrySet()) {
			if (entry.getValue() > 0) {
				list.add(entry);
			}
		}

		// 점수 높은 순으로 정렬
		list.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

		// 상위 N개만 DTO로 만들어서 반환
		for (int i = 0; i < Math.min(list.size(), limit); i++) {
			Map.Entry<String, Double> entry = list.get(i);
			String keyword = entry.getKey();
			Long score = entry.getValue().longValue();
			result.add(new KeywordRankingDto(keyword, score));
		}

		return result;
	}
}