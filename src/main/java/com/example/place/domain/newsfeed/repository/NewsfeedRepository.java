package com.example.place.domain.newsfeed.repository;

import com.example.place.domain.newsfeed.entity.Newsfeed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {

	Page<Newsfeed> findByIsDeletedFalse(Pageable pageable);

	// fetch join 사용
	@Query("SELECT DISTINCT n FROM Newsfeed n " +
		"JOIN FETCH n.user " +
		"WHERE n.isDeleted = false")
	Page<Newsfeed> findByIsDeletedFalseWithFetchJoin(Pageable pageable);
}
