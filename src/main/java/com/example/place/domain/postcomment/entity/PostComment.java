package com.example.place.domain.postcomment.entity;


import com.example.place.common.entity.BaseEntity;
import com.example.place.common.entity.SoftDeleteEntity;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.user.entity.User;

import jakarta.persistence.Entity;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "postcomments")
public class PostComment extends SoftDeleteEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	private String content;

	private PostComment(User user, Post post, String content) {
		this.user = user;
		this.post = post;
		this.content = content;
	}

	public static PostComment of(User user, Post post, String content) {
		return new PostComment(user, post, content);
	}

	public void updateContent(String content) {
		this.content = content;
	}
}
