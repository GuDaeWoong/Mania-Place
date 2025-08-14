package com.example.place.domain.keyword.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.place.domain.keyword.domain.model.KeywordSnapshot;
import com.example.place.domain.keyword.domain.repository.SearchKeywordSnapshotRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeywordSnapshotService {

	private final RedisTemplate<String, String> redisTemplate;
	private final SearchKeywordSnapshotRepository snapshotRepository;

	private static final String KEYWORD_ZSET = "keyword_rankings";
	private static final DateTimeFormatter KEY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");

	public void createHourlySnapshot() {
		LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
		String key = KEYWORD_ZSET + ":" + now.format(KEY_FORMATTER);

		Set<String> keywords = redisTemplate.opsForZSet().range(key, 0, -1);
		if (keywords == null || keywords.isEmpty()) {
			log.info("스냅샷 생성할 키워드가 없습니다. 키: {}", key);
			return;
		}

		List<KeywordSnapshot> snapshots = keywords.stream()
			.map(keyword -> {
				Double score = redisTemplate.opsForZSet().score(key, keyword);
				long count = (score == null) ? 0L : score.longValue();
				return KeywordSnapshot.of(keyword, now, count);
			})
			.collect(Collectors.toList());

		snapshotRepository.saveAll(snapshots);
		log.info("스냅샷 저장 완료 - 키: {}, 데이터 수: {}", key, snapshots.size());
	}
}