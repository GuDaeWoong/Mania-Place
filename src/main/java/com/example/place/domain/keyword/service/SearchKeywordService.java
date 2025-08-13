package com.example.place.domain.keyword.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

	/**
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

		} catch (Exception e) {
			log.error("인기 키워드 조회 실패", e);
			return Collections.emptyList();
		}
	}
}