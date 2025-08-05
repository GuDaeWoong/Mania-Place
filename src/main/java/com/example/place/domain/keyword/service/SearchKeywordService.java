package com.example.place.domain.keyword.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.domain.keyword.domain.model.KeywordSnapshot;
import com.example.place.domain.keyword.domain.model.SearchKeyword;
import com.example.place.domain.keyword.domain.repository.SearchKeywordRepository;
import com.example.place.domain.keyword.domain.repository.SearchKeywordSnapshotRepository;
import com.example.place.domain.keyword.service.dto.KeywordRankingDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchKeywordService {

	private final SearchKeywordRepository searchKeywordRepository;
	private final SearchKeywordSnapshotRepository searchKeywordSnapshotRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void addKeyword(String keyword) {
		// 정제 메소드 및 Null 체크
		if (keyword == null || keyword.trim().isEmpty()) {
			log.warn("빈 키워드 무시됨");
			return;
		}

		String normalizedKeyword = keyword.toLowerCase().trim();

		// 키워드 길이 제한
		if (normalizedKeyword.length() > 100) {
			log.warn("키워드 길이 초과: {}", normalizedKeyword.substring(0, 20) + "...");
			return;
		}

		try {
			Optional<SearchKeyword> searchKeyword = searchKeywordRepository.findById(normalizedKeyword);

			if (searchKeyword.isPresent()) {
				SearchKeyword existingKeyword = searchKeyword.get();
				existingKeyword.incrementCount();
				searchKeywordRepository.save(existingKeyword);
				log.info("키워드 카운트 증가: {} -> {}", existingKeyword.getKeyword(), existingKeyword.getCount());
			} else {
				SearchKeyword newKeyword = SearchKeyword.of(normalizedKeyword);
				searchKeywordRepository.save(newKeyword);
				log.info("새 키워드 등록: {}", newKeyword.getKeyword());
			}
		} catch (Exception e) {
			log.error("키워드 저장 실패: {}", normalizedKeyword, e);
		}
	}

	@Transactional
	public void createSnapshot() {
		LocalDateTime now = LocalDateTime.now();

		try {
			List<SearchKeyword> keywords = searchKeywordRepository.findAll();

			if (keywords.isEmpty()) {
				log.info("스냅샷 생성할 키워드가 없음");
				return;
			}

			List<KeywordSnapshot> snapshots = keywords.stream()
				.map(k -> KeywordSnapshot.of(k.getKeyword(), now, k.getCount()))
				.collect(Collectors.toList());

			searchKeywordSnapshotRepository.saveAll(snapshots);
			log.info("스냅샷 생성 완료: {}개 키워드, 시간: {}", snapshots.size(), now);

		} catch (Exception e) {
			log.error("스냅샷 생성 실패", e);
			throw e;
		}
	}

	// limit : 최대 페이지 개수
	@Transactional(readOnly = true)
	public List<KeywordRankingDto> getTopKeywordsLast24Hours(int limit) {
		LocalDateTime now = LocalDateTime.now();
		// LocalDateTime before24h = now.minusHours(24);
		// 테스트용 2분
		LocalDateTime before24h = now.minusMinutes(2);

		try {
			List<KeywordSnapshot> currentSnapshots = searchKeywordSnapshotRepository.findLatestSnapshotsBefore(now);
			List<KeywordSnapshot> pastSnapshots = searchKeywordSnapshotRepository.findLatestSnapshotsBefore(before24h);

			if (currentSnapshots.isEmpty()) {
				log.warn("현재 스냅샷이 없음 - 실시간 데이터로 대체");
				return getTopKeywords(limit); // 실시간 데이터로 대체
			}

			Map<String, Long> currentMap = currentSnapshots.stream()
				.collect(Collectors.toMap(KeywordSnapshot::getKeyword, KeywordSnapshot::getCount));

			Map<String, Long> pastMap = pastSnapshots.stream()
				.collect(Collectors.toMap(KeywordSnapshot::getKeyword, KeywordSnapshot::getCount));

			Set<String> allKeywords = new HashSet<>();
			allKeywords.addAll(currentMap.keySet());
			allKeywords.addAll(pastMap.keySet());

			return allKeywords.stream()
				.map(keyword -> {
					long currentCount = currentMap.getOrDefault(keyword, 0L);
					long pastCount = pastMap.getOrDefault(keyword, 0L);
					long diff = currentCount - pastCount;
					return new KeywordRankingDto(keyword, diff);
				})
				.filter(dto -> dto.getCount() > 0) // 증가량이 0 이하인 키워드 제외 (변동 없는 키워드 제거)
				.sorted((a, b) -> Long.compare(b.getCount(), a.getCount())) // 내림차순
				.limit(limit) // 최대 랭킹 수
				.collect(Collectors.toList());

		} catch (Exception e) {
			log.error("급상승 키워드 조회 실패", e);
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

		} catch (Exception e) {
			log.error("인기 키워드 조회 실패", e);
			return Collections.emptyList();
		}
	}
}