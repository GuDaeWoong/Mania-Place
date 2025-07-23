package com.example.place.domain.itemcomment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.place.domain.itemcomment.entity.ItemComment;

public interface ItemCommentRepository extends JpaRepository<ItemComment, Long> {
}
