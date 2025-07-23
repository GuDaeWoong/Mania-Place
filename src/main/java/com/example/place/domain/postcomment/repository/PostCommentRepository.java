package com.example.place.domain.postcomment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.place.domain.postcomment.entity.PostComment;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}
