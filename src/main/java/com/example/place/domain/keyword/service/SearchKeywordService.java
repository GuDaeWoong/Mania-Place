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

			// 2. Redis에서 여러 ZSet 합산 후 상위 N개 바로 조회
			return getTopRankedKeywords(hourlyKeys, limit);
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

	// 기존 getTopRankedKeywords 메서드 주석 처리
	/*
	private List<KeywordRankingDto> getTopRankedKeywords(List<String> keys, int limit) {
		if (keys.isEmpty()) {
			return Collections.emptyList();
		}

		String tempKey = "temp:ranking:" + UUID.randomUUID();

		try {
			// ZUNIONSTORE: keys의 모든 ZSet을 합산해 tempKey에 저장
			unionZSets(keys, tempKey);

			// 상위 N개 점수 높은 순으로 조회
			Set<ZSetOperations.TypedTuple<String>> topEntries =
				redisTemplate.opsForZSet().reverseRangeWithScores(tempKey, 0, limit - 1);

			return convertToRankingList(topEntries);

		} finally {
			// 임시 키 삭제
			redisTemplate.delete(tempKey);
		}
	}
	*/

	/**
	 * Redis ZUNIONSTORE 활용: 여러 키를 합산 후 상위 N개 조회 (안전한 버전)
	 */
	private List<KeywordRankingDto> getTopRankedKeywords(List<String> keys, int limit) {
		if (keys.isEmpty()) {
			return Collections.emptyList();
		}

		// 실제 존재하는 키만 필터링
		Set<String> existingKeys = redisTemplate.keys(KEYWORD_ZSET + ":*");
		List<String> validKeys = keys.stream()
			.filter(existingKeys::contains)
			.collect(Collectors.toList());

		log.info("요청된 키 개수: {}, 실제 존재하는 키 개수: {}", keys.size(), validKeys.size());

		if (validKeys.isEmpty()) {
			log.info("유효한 키가 없어 빈 결과 반환");
			return Collections.emptyList();
		}

		// 키가 1개인 경우 union 없이 직접 조회
		if (validKeys.size() == 1) {
			String singleKey = validKeys.get(0);
			Set<ZSetOperations.TypedTuple<String>> topEntries =
				redisTemplate.opsForZSet().reverseRangeWithScores(singleKey, 0, limit - 1);
			log.info("단일 키 직접 조회 결과: {} 개", topEntries != null ? topEntries.size() : 0);
			return convertToRankingList(topEntries);
		}

		// 여러 키인 경우 ZUNIONSTORE 사용
		String tempKey = "temp:ranking:" + UUID.randomUUID();

		try {
			// 각 키의 데이터 존재 여부 재확인
			List<String> nonEmptyKeys = new ArrayList<>();
			for (String key : validKeys) {
				Long count = redisTemplate.opsForZSet().count(key, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
				if (count != null && count > 0) {
					nonEmptyKeys.add(key);
				}
			}

			log.info("데이터가 있는 키 개수: {}", nonEmptyKeys.size());

			if (nonEmptyKeys.isEmpty()) {
				return Collections.emptyList();
			}

			if (nonEmptyKeys.size() == 1) {
				// 결국 키가 1개만 남으면 직접 조회
				Set<ZSetOperations.TypedTuple<String>> topEntries =
					redisTemplate.opsForZSet().reverseRangeWithScores(nonEmptyKeys.get(0), 0, limit - 1);
				return convertToRankingList(topEntries);
			}

			// ZUNIONSTORE 실행
			log.info("ZUNIONSTORE 실행 - 키 개수: {}", nonEmptyKeys.size());
			unionZSets(nonEmptyKeys, tempKey);

			// 상위 N개 점수 높은 순으로 조회
			Set<ZSetOperations.TypedTuple<String>> topEntries =
				redisTemplate.opsForZSet().reverseRangeWithScores(tempKey, 0, limit - 1);

			log.info("ZUNIONSTORE 결과: {} 개", topEntries != null ? topEntries.size() : 0);
			return convertToRankingList(topEntries);

		} catch (Exception e) {
			log.error("ZUNIONSTORE 실행 실패, 개별 키 조회로 대체", e);

			// ZUNIONSTORE 실패 시 대체 로직: 각 키에서 개별 조회 후 수동 합산
			return getTopKeywordsManually(validKeys, limit);

		} finally {
			// 임시 키 삭제
			try {
				redisTemplate.delete(tempKey);
			} catch (Exception e) {
				log.warn("임시 키 삭제 실패: {}", tempKey);
			}
		}
	}

	/**
	 * ZUNIONSTORE 실패 시 대체 방법: 수동 합산
	 */
	private List<KeywordRankingDto> getTopKeywordsManually(List<String> keys, int limit) {
		log.info("수동 합산 방식으로 키워드 랭킹 조회");

		Map<String, Long> keywordScores = new HashMap<>();

		for (String key : keys) {
			try {
				// 각 키에서 모든 데이터 조회
				Set<ZSetOperations.TypedTuple<String>> keyData =
					redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);

				if (keyData != null) {
					for (ZSetOperations.TypedTuple<String> tuple : keyData) {
						if (tuple.getValue() != null && tuple.getScore() != null) {
							keywordScores.merge(tuple.getValue(),
								tuple.getScore().longValue(),
								Long::sum);
						}
					}
				}
			} catch (Exception e) {
				log.warn("키 {} 조회 실패", key, e);
			}
		}

		// 점수순으로 정렬 후 상위 N개 반환
		return keywordScores.entrySet().stream()
			.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
			.limit(limit)
			.map(entry -> new KeywordRankingDto(entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());
	}

	// 기존 unionZSets 메서드 주석 처리
	/*
	private void unionZSets(List<String> keys, String tempKey) {
		if (keys.size() == 1) {
			// 키가 1개면 union할 필요 없이 복사
			redisTemplate.opsForZSet().unionAndStore(keys.get(0), Collections.emptySet(), tempKey);
		} else {
			redisTemplate.opsForZSet().unionAndStore(keys.get(0), keys.subList(1, keys.size()), tempKey);
		}
	}
	*/

	/**
	 * ZSet 합산 처리 (안전한 버전)
	 */
	private void unionZSets(List<String> keys, String tempKey) {
		if (keys.size() == 1) {
			// 키가 1개면 복사
			redisTemplate.opsForZSet().unionAndStore(keys.get(0), Collections.emptySet(), tempKey);
		} else {
			// 여러 키 합산
			String firstKey = keys.get(0);
			Collection<String> otherKeys = keys.subList(1, keys.size());
			redisTemplate.opsForZSet().unionAndStore(firstKey, otherKeys, tempKey);
		}
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