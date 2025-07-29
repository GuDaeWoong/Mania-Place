package com.example.place.domain.post.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.place.domain.post.entity.Post;
import com.example.place.domain.user.entity.User;

public interface PostRepository extends JpaRepository<Post, Long> {

	@Query(
		value = "SELECT DISTINCT p FROM Post p " +
			"JOIN FETCH p.user " +
			"JOIN FETCH p.item ",
		countQuery = "SELECT COUNT(p) FROM Post p"
	)
	Page<Post> findAll(Pageable pageable);

	@Query(
		value = """
        SELECT DISTINCT p FROM Post p
        JOIN FETCH p.user
        JOIN FETCH p.item
        WHERE p.user = :user AND p.isDeleted = false
        """,
		countQuery = """
        SELECT COUNT(p) FROM Post p
        WHERE p.user = :user AND p.isDeleted = false
        """
	)
	Page<Post> findAllByUserAndIsDeletedFalse(@Param("user") User user, Pageable pageable);


	@Query(
		value = """
        SELECT DISTINCT p FROM Post p
        JOIN FETCH p.user
        JOIN FETCH p.item
        WHERE p.isDeleted = false
        """,
		countQuery = """
        SELECT COUNT(p) FROM Post p
        WHERE p.isDeleted = false
        """
	)
	Page<Post> findByItemIdAndIsDeletedFalse(Pageable pageable);

	Optional<Post> findByIdAndIsDeletedFalse(Long id);
}