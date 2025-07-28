package com.example.place.domain.vote.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.place.domain.post.entity.Post;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.vote.entity.Vote;
import com.example.place.domain.vote.entity.VoteType;

public interface VoteRepository extends JpaRepository<Vote, Long> {
	Optional<Vote> findByUserAndPostAndVoteType(User user, Post post, VoteType attr0);

	@Query("""
		SELECT
		    COUNT(CASE WHEN v.voteType = 'LIKE' THEN 1 END),
		    COUNT(CASE WHEN v.voteType = 'DISLIKE' THEN 1 END),
		    COALESCE(MAX(CASE WHEN v.voteType = 'LIKE' AND v.user.id = :userId THEN 1 ELSE 0 END), 0),
		    COALESCE(MAX(CASE WHEN v.voteType = 'DISLIKE' AND v.user.id = :userId THEN 1 ELSE 0 END), 0)
		FROM Vote v
		JOIN v.post p
		WHERE p.id = :postId""")
	List<Object[]> findVoteStatsByPostIds(@Param("postId") Long postId, @Param("userId") Long userId);

	@Query("""
       SELECT
           p.id,
           COUNT(CASE WHEN v.voteType = 'LIKE' THEN 1 END),
           COUNT(CASE WHEN v.voteType = 'DISLIKE' THEN 1 END),
           COALESCE(MAX(CASE WHEN v.voteType = 'LIKE' AND v.user.id = :userId THEN 1 ELSE 0 END), 0),
           COALESCE(MAX(CASE WHEN v.voteType = 'DISLIKE' AND v.user.id = :userId THEN 1 ELSE 0 END), 0)
       FROM Post p
       LEFT JOIN Vote v ON p.id = v.post.id
       WHERE p.id IN :postIds
       GROUP BY p.id
       """)
	List<Object[]> findVoteStatsByPostIdsAndUser(@Param("postIds") List<Long> postIds, @Param("userId") Long userId);
}
