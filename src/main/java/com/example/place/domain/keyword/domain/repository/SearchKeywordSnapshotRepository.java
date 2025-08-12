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

	// 데이터 정리
	@Modifying
	@Transactional
	void deleteBySnapshotTimeBefore(LocalDateTime time);
}