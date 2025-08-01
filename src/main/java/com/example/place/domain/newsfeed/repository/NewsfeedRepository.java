package com.example.place.domain.newsfeed.repository;

import java.util.Optional;

import com.example.place.domain.newsfeed.entity.Newsfeed;
import com.example.place.domain.user.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {

	Page<Newsfeed> findByIsDeletedFalse(Pageable pageable);

	// NewsfeedRepository에 추가
	@Query("SELECT n FROM Newsfeed n LEFT JOIN FETCH n.images WHERE n.id = :id")
	Optional<Newsfeed> findByIdWithImages(@Param("id") Long id);

}
