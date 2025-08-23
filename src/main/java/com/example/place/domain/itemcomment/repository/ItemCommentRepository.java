package com.example.place.domain.itemcomment.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.place.domain.itemcomment.entity.ItemComment;

public interface ItemCommentRepository extends JpaRepository<ItemComment, Long> {

	@Query(
		value = "SELECT c FROM ItemComment c "
			+ "JOIN FETCH c.user "
			+ "WHERE c.item.id = :itemId AND c.isDeleted = false",
		countQuery = "SELECT count(c) FROM ItemComment c "
			+ "WHERE c.item.id = :itemId AND c.isDeleted = false"
	)
	Page<ItemComment> findByItemIdAndIsDeletedFalse(Long itemId, Pageable pageable);

	List<ItemComment> findByItemId(Long itemId);
}