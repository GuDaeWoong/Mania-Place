package com.example.place.domain.keyword;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.example.place.domain.keyword.domain.repository.SearchKeywordSnapshotRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeywordCleanupScheduler {

	private final SearchKeywordSnapshotRepository snapshotRepository;

	/**
	 * 매주 일요일 새벽 2시에 오래된 스냅샷 정리
	 * 0 0 2 * * SUN = 매주 일요일 오전 2시
	 */
	@Scheduled(cron = "0 0 2 * * SUN")
	@Transactional
	public void cleanupOldSnapshots() {
		try {
			log.info(" 오래된 스냅샷 정리 시작");

			// 30일 이전 데이터 삭제
			LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);

			snapshotRepository.deleteBySnapshotTimeBefore(cutoffDate);

			// 임시로 로그만 출력
			log.info(" {}일 이전 스냅샷 정리 대상 조회", cutoffDate.toLocalDate());
			log.info(" 스냅샷 정리 완료");

		} catch (Exception e) {
			log.error(" 스냅샷 정리 실패: {}", e.getMessage(), e);
		}
	}
}