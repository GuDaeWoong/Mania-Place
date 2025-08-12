package com.example.place.domain.keyword.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	private List<String> getLast24HourKeys() {
		List<String> keys = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();

		for (int i = 0; i < 24; i++) {
			LocalDateTime targetHour = now.minusHours(i);
			String key = KEYWORD_ZSET + ":" + targetHour.format(KEY_FORMATTER);
			keys.add(key);
		}

		return keys;
	}

	/**
	 * 키워드 추가 (현재 시간대별 키에 점수 1 증가)
	 */
	@Transactional
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
			redisTemplate.opsForZSet().incrementScore(redisKey, normalizedKeyword, 1);
			// TTL 24시간 설정
			redisTemplate.expire(redisKey, Duration.ofHours(24));
			log.info("Redis 시간별 ZSet 점수 증가: {}, 키: {}", normalizedKeyword, redisKey);
		} catch (Exception e) {
			log.error("Redis 키워드 점수 증가 실패: {}", normalizedKeyword, e);
		}
	}

	/**
	 * 24시간 급상승 키워드 조회 (시간별 키 24개 합산)
	 */
	@Loggable
	@Transactional(readOnly = true)
	public List<KeywordRankingDto> getTopKeywordsLast24Hours(int limit) {
		try {
			List<String> last24HourKeys = getLast24HourKeys();

			Map<String, Double> scoreMap = new HashMap<>();

			for (String key : last24HourKeys) {
				Set<String> keywords = redisTemplate.opsForZSet().range(key, 0, -1);
				if (keywords == null || keywords.isEmpty()) continue;

				for (String keyword : keywords) {
					Double score = redisTemplate.opsForZSet().score(key, keyword);
					if (score != null) {
						scoreMap.put(keyword, scoreMap.getOrDefault(keyword, 0.0) + score);
					}
				}
			}

			return scoreMap.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
				.limit(limit)
				.map(entry -> new KeywordRankingDto(entry.getKey(), entry.getValue().longValue()))
				.collect(Collectors.toList());

		} catch (Exception e) {
			log.error("24시간 급상승 키워드 조회 실패", e);
			return Collections.emptyList();
		}
	}
}