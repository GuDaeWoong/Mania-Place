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
			// 최근 24시간 동안의 Redis 키 리스트를 생성한다.
			// 예를 들어 "keyword_rankings:2025-08-12-19" 와 같은 형식의 키가 24개 생성됨
			List<String> last24HourKeys = getLast24HourKeys();

			// 키워드별 누적 점수를 저장할 맵을 초기화한다.
			// 키: 키워드 문자열, 값: 점수(인기도)
			Map<String, Double> scoreMap = new HashMap<>();

			// 각 시간별 Redis 키에 대해 반복 처리
			for (String key : last24HourKeys) {
				// 해당 시간대 키의 모든 키워드 목록을 조회한다.
				Set<String> keywords = redisTemplate.opsForZSet().range(key, 0, -1);
				// 조회 결과가 없으면 다음 시간대로 넘어간다.
				if (keywords == null || keywords.isEmpty()) continue;

				// 조회된 키워드들 각각에 대해 반복
				for (String keyword : keywords) {
					// 해당 키워드의 점수를 조회한다.
					Double score = redisTemplate.opsForZSet().score(key, keyword);
					if (score != null) {
						// 기존 점수에 이번 시간대 점수를 누적해서 저장한다.
						// 같은 키워드가 여러 시간대에 존재할 수 있으므로 합산 필요
						scoreMap.put(keyword, scoreMap.getOrDefault(keyword, 0.0) + score);
					}
				}
			}

			// 누적된 점수를 기준으로 내림차순 정렬한다.
			// 그리고 상위 limit 개의 키워드를 추출한다.
			// 추출된 키워드는 KeywordRankingDto 객체로 변환되어 리스트로 반환된다.
			return scoreMap.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
				.limit(limit)
				.map(entry -> new KeywordRankingDto(entry.getKey(), entry.getValue().longValue()))
				.collect(Collectors.toList());

		} catch (Exception e) {
			// Redis 접근 중 오류 발생 시 에러 로그를 남기고 빈 리스트를 반환한다.
			// 서비스 전체가 중단되는 것을 방지하기 위한 예외 처리
			log.error("24시간 급상승 키워드 조회 실패", e);
			return Collections.emptyList();
		}
	}
}