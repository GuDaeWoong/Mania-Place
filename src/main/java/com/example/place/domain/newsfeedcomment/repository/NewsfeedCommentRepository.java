package com.example.place.domain.newsfeedcomment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.place.domain.newsfeed.entity.Newsfeed;
import com.example.place.domain.newsfeedcomment.entity.NewsfeedComment;

public interface NewsfeedCommentRepository extends JpaRepository<NewsfeedComment, Long> {
	Optional<NewsfeedComment> findByIdAndIsDeletedFalse(Long id);

	@EntityGraph(attributePaths = {"user"})
	Page<NewsfeedComment> findAllByNewsfeedAndIsDeletedFalse(Newsfeed newsfeed, Pageable pageable);

	List<NewsfeedComment> findByNewsfeedIdAndIsDeletedFalse(Long newsfeedId);
}
