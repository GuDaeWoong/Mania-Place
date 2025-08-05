package com.example.place.domain.keyword.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.domain.keyword.domain.model.KeywordSnapshot;

public interface SearchKeywordSnapshotRepository extends JpaRepository<KeywordSnapshot, Long> {

	// 최근 스냅샷 가져오기
	@Query("SELECT ks FROM KeywordSnapshot ks WHERE ks.snapshotTime = " +
		"(SELECT MAX(ks2.snapshotTime) FROM KeywordSnapshot ks2 WHERE ks2.snapshotTime <= :time)")
	List<KeywordSnapshot> findLatestSnapshotsBefore(@Param("time") LocalDateTime time);

	// 데이터 정리
	@Modifying
	@Transactional
	void deleteBySnapshotTimeBefore(LocalDateTime time);
}