package com.example.place.domain.keyword;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.place.domain.keyword.service.SearchKeywordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeywordSnapshotScheduler {

	private final SearchKeywordService searchKeywordService;

	// /**
	//  * 매시간 정각에 스냅샷 생성
	//  * cron: "초 분 시 일 월 요일"
	//  * 0 0 * * * * = 매시간 0분 0초
	//  */
	// @Scheduled(cron = "0 0 * * * *")
	// public void createHourlySnapshot() {
	// 	try {
	// 		log.info("정시 스냅샷 생성 시작");
	// 		searchKeywordService.createSnapshot();
	// 		log.info("정시 스냅샷 생성 완료");
	// 	} catch (Exception e) {
	// 		log.error("정시 스냅샷 생성 실패: {}", e.getMessage(), e);
	// 	}
	// }

	/**
	 * 테스트용: 매 5분마다 (개발 중에만 사용)
	 * 0 5 * * * * = 매 5분
     */
	@Scheduled(cron = "0 */1 * * * *")
	public void createTestSnapshot() {
		try {
			log.info("테스트 스냅샷 생성 시작");
			searchKeywordService.createSnapshot();
			log.info("테스트 스냅샷 생성 완료");
		} catch (Exception e) {
			log.error("테스트 스냅샷 생성 실패: {}", e.getMessage(), e);
		}
	}
}