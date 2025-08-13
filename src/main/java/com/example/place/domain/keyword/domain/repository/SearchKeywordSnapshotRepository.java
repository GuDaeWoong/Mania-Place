package com.example.place.domain.keyword.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.domain.keyword.domain.model.KeywordSnapshot;

public interface SearchKeywordSnapshotRepository extends JpaRepository<KeywordSnapshot, Long> {

	// 데이터 정리
	@Modifying
	@Transactional
	void deleteBySnapshotTimeBefore(LocalDateTime time);

		/**
		 * 24시간 범위 내 스냅샷 조회
		 *
		 * @param from 조회 시작 시간
		 * @param to   조회 종료 시간
		 * @return 시간 범위 내 스냅샷 리스트
		 */
		@Query("SELECT ks FROM KeywordSnapshot ks WHERE ks.snapshotTime BETWEEN :from AND :to")
		List<KeywordSnapshot> findSnapshotsBetween(@Param("from") LocalDateTime from,
			@Param("to") LocalDateTime to);
}