package com.example.place.domain.postcomment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.place.domain.post.entity.Post;
import com.example.place.domain.postcomment.entity.PostComment;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
	@EntityGraph(attributePaths = {"user"})
	Page<PostComment> findAllByPost(Post post, Pageable pageable);
}
