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
			// 최근 24시간 동안 Redis에 저장된 키워드 점수 키 리스트 생성
			// 예: "keyword_rankings:2025-08-12-19" 같은 키가 24개 만들어짐
			List<String> last24HourKeys = getLast24HourKeys();

			// 키워드별 누적 점수를 저장할 맵 생성
			// key: 키워드 문자열, value: 점수 (검색 횟수)
			Map<String, Double> scoreMap = new HashMap<>();

			// 24시간 각각의 키를 순회하며
			for (String key : last24HourKeys) {
				// 해당 시간대 키워드 목록을 모두 가져옴
				Set<String> keywords = redisTemplate.opsForZSet().range(key, 0, -1);
				if (keywords == null || keywords.isEmpty()) {
					// 키워드가 없으면 다음 시간대로 넘어감
					continue;
				}

				// 각 키워드에 대해
				for (String keyword : keywords) {
					// 해당 키워드의 점수를 가져옴
					Double score = redisTemplate.opsForZSet().score(key, keyword);
					if (score != null) {
						// 이미 있던 점수에 이번 시간대 점수를 더해서 누적
						scoreMap.put(keyword, scoreMap.getOrDefault(keyword, 0.0) + score);
					}
				}
			}

			// 점수 내림차순으로 정렬한 후, 상위 limit개를 선택
			// 선택한 키워드와 점수를 KeywordRankingDto 객체로 만들어 리스트 반환
			return scoreMap.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
				.limit(limit)
				.map(entry -> new KeywordRankingDto(entry.getKey(), entry.getValue().longValue()))
				.collect(Collectors.toList());

		} catch (Exception e) {
			// 오류가 발생하면 로그에 남기고 빈 리스트 반환 (서비스가 멈추지 않도록 함)
			log.error("24시간 인기 키워드 조회 중 오류 발생", e);
			return Collections.emptyList();
		}
	}
}