package com.example.place.domain.itemcomment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.place.domain.itemcomment.entity.ItemComment;

public interface ItemCommentRepository extends JpaRepository<ItemComment, Long> {

	@EntityGraph(attributePaths = {"user"})
	Page<ItemComment> findByItemId(Long itemId, Pageable pageable);
}
