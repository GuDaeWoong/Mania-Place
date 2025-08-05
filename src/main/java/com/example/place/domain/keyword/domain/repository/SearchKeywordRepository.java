package com.example.place.domain.keyword.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.place.domain.keyword.domain.model.SearchKeyword;

public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, String> {

	@Query("SELECT sk FROM SearchKeyword sk ORDER BY sk.count DESC")
	List<SearchKeyword> findAllByOrderByCountDesc(Pageable pageable);

	@Modifying
	@Query("DELETE FROM SearchKeyword k WHERE k.count <= 1 OR k.updatedAt < :cutoffDate")
	int deleteLowCountOrOldKeywords(@Param("cutoffDate") LocalDateTime cutoffDate);
}