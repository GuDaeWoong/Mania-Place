package com.example.place.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.place.domain.post.entity.Post;
import com.example.place.domain.user.entity.User;

public interface PostRepository extends JpaRepository<Post, Long> {

	Page<Post> findAllByUser(User user, Pageable pageable);

}
