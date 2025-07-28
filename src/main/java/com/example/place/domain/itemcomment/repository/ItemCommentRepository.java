package com.example.place.domain.itemcomment.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.place.domain.itemcomment.entity.ItemComment;

public interface ItemCommentRepository extends JpaRepository<ItemComment, Long> {

	@EntityGraph(attributePaths = {"user"})
	Page<ItemComment> findPageByItemId(Long itemId, Pageable pageable);

	List<ItemComment> findByItemId(Long itemId);
}
