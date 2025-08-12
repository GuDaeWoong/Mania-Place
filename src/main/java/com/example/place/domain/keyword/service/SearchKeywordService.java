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

	/**
	 * 키워드 추가
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
			redisTemplate.expire(redisKey, Duration.ofHours(24));
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
	@Transactional(readOnly = true)
	public List<KeywordRankingDto> getTopKeywordsLast24Hours(int limit) {
		try {
			LocalDateTime now = LocalDateTime.now();

			// 지금 시간 키
			String nowKey = KEYWORD_ZSET + ":" + now.format(KEY_FORMATTER);

			// 24시간 전 키
			String ago24Key = KEYWORD_ZSET + ":" + now.minusHours(24).format(KEY_FORMATTER);

			// 지금 시간과 24시간 전 모든 키워드 가져오기
			Set<String> nowKeywords = redisTemplate.opsForZSet().range(nowKey, 0, -1);
			Set<String> ago24Keywords = redisTemplate.opsForZSet().range(ago24Key, 0, -1);

			// 모든 키워드 합치기
			Set<String> allKeywords = new HashSet<>();
			if (nowKeywords != null) allKeywords.addAll(nowKeywords);
			if (ago24Keywords != null) allKeywords.addAll(ago24Keywords);

			Map<String, Double> result = new HashMap<>();

			for (String keyword : allKeywords) {
				// 현재 시간 키에서 해당 키워드 점수 조회 (null이면 0.0)
				Double nowScoreObj = redisTemplate.opsForZSet().score(nowKey, keyword);
				double nowScore = (nowScoreObj != null) ? nowScoreObj : 0.0;

				// 24시간 전 키에서 해당 키워드 점수 조회 (null이면 0.0)
				Double ago24ScoreObj = redisTemplate.opsForZSet().score(ago24Key, keyword);
				double ago24Score = (ago24ScoreObj != null) ? ago24ScoreObj : 0.0;

				// 현재 점수 - 24시간 전 점수 = 실제 24시간 동안 증가한 점수 계산
				double real24HourCount = nowScore - ago24Score;

				// 증가한 점수가 0보다 클 때만 결과 맵에 저장
				if (real24HourCount > 0) {
					result.put(keyword, real24HourCount);
				}
			}

			// 점수 높은 순으로 정렬해서 리턴
			return result.entrySet().stream()
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