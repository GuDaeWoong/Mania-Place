package com.example.place.domain.keyword.service;

import java.time.Duration;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
			if (ttl == null || ttl < 0) {
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
	 * Redis ZUNIONSTORE 활용: 여러 키를 합산 후 상위 N개 조회
	 */
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

	/**
	 * ZSet 합산 처리
	 */
	private void unionZSets(List<String> keys, String tempKey) {
		if (keys.size() == 1) {
			// 키가 1개면 union할 필요 없이 복사
			redisTemplate.opsForZSet().unionAndStore(keys.get(0), Collections.emptySet(), tempKey);
		} else {
			redisTemplate.opsForZSet().unionAndStore(keys.get(0), keys.subList(1, keys.size()), tempKey);
		}
	}

	 * 최근 24시간 동안 검색량이 많은 키워드 상위 N개 조회 (중간 스냅샷 포함)
	 *
	 * @param limit 상위 N개 키워드 개수
	 * @return 검색량 기준 내림차순 정렬된 키워드 랭킹 리스트
	 */
	@Transactional(readOnly = true)
	public List<KeywordRankingDto> getTopKeywordsLast24Hours(int limit) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime from = now.minusHours(24); // 24시간 전 시점

		try {
			// 1. 24시간 범위 내 모든 스냅샷 조회
			List<KeywordSnapshot> snapshots = searchKeywordSnapshotRepository.findSnapshotsBetween(from, now);

			// 2. 스냅샷 없으면 실시간 누적 데이터 사용
			if (snapshots.isEmpty()) {
				log.warn("24시간 스냅샷 없음 - 실시간 누적 데이터로 대체");
				return getTopKeywords(limit);
			}

			// 3. 키워드별 스냅샷 그룹화
			Map<String, List<KeywordSnapshot>> keywordGroupMap = snapshots.stream()
				.collect(Collectors.groupingBy(KeywordSnapshot::getKeyword));

			List<KeywordRankingDto> ranking = new ArrayList<>();

			// 4. 키워드별 24시간 검색량 계산
			for (Map.Entry<String, List<KeywordSnapshot>> entry : keywordGroupMap.entrySet()) {
				String keyword = entry.getKey();
				List<KeywordSnapshot> keywordSnapshots = entry.getValue();

				// 시간순 정렬
				keywordSnapshots.sort(Comparator.comparing(KeywordSnapshot::getSnapshotTime));

				// 증가량 합산
				long totalIncrease = 0;
				KeywordSnapshot prev = null;
				for (KeywordSnapshot curr : keywordSnapshots) {
					if (prev != null) {
						totalIncrease += curr.getCount() - prev.getCount();
					}
					prev = curr;
				}

				// 0보다 큰 키워드만 랭킹에 포함
				if (totalIncrease > 0) {
					ranking.add(new KeywordRankingDto(keyword, totalIncrease));
				}
			}

			// 5. 내림차순 정렬 후 상위 N개 반환
			return ranking.stream()
				.sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
				.limit(limit)
				.collect(Collectors.toList());

		} catch (Exception e) {
			log.error("24시간 급상승 키워드 조회 오류", e);
			return Collections.emptyList();
		}
	}


	// 스냅샷이 없을때
	@Transactional(readOnly = true)
	public List<KeywordRankingDto> getTopKeywords(int limit) {
		try {
			Pageable pageable = PageRequest.of(0, limit);
			List<SearchKeyword> keywords = searchKeywordRepository.findAllByOrderByCountDesc(pageable);

			return keywords.stream()
				.map(k -> new KeywordRankingDto(k.getKeyword(), k.getCount()))
				.collect(Collectors.toList());

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