package com.example.place.domain.vote.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.place.domain.post.entity.Post;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.vote.entity.Vote;
import com.example.place.domain.vote.entity.VoteType;

public interface VoteRepository extends JpaRepository<Vote, Long> {
	Optional<Vote> findByUserAndPostAndVoteType(User user, Post post, VoteType attr0);

	Long countByPostAndVoteType(Post post, VoteType voteType);
}
