package com.example.place.domain.newsfeedcomment.entity;

import com.example.place.common.entity.SoftDeleteEntity;
import com.example.place.domain.newsfeed.entity.Newsfeed;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.postcomment.entity.PostComment;
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
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "newsfeedcomments")
public class NewsfeedComment extends SoftDeleteEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "newsfeed_id")
	private Newsfeed newsfeed;

	private String content;

	private NewsfeedComment(User user, Newsfeed newsfeed, String content) {
		this.user = user;
		this.newsfeed = newsfeed;
		this.content = content;
	}

	public static NewsfeedComment of(User user, Newsfeed newsfeed, String content) {
		return new NewsfeedComment(user, newsfeed, content);
	}

	public void updateContent(String content) {
		this.content = content;
	}
}
