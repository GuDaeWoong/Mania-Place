package com.example.place.domain.vote.entity;

import com.example.place.domain.post.entity.Post;
import com.example.place.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "votes")
public class Vote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	@Enumerated(EnumType.STRING)
	private VoteType voteType;

	private Vote(User user, Post post, VoteType newVoteType) {
		this.user = user;
		this.post = post;
		this.voteType = newVoteType;
	}

	public static Vote of(User user, Post post, VoteType newVoteType) {
		return new Vote(user, post, newVoteType);
	}
}
