package com.example.place.domain.postcomment.dto;

import java.time.LocalDateTime;


import com.example.place.domain.postcomment.entity.PostComment;
import com.example.place.domain.user.entity.User;

import lombok.Getter;

@Getter
public class PostCommentResponse {
	private String nickname;
	private String userImageUrl;
	private String content;
	private LocalDateTime createAt;

	private PostCommentResponse(String nickname, String userImageUrl, String content, LocalDateTime createAt) {
		this.nickname = nickname;
		this.userImageUrl = userImageUrl;
		this.content = content;
		this.createAt = createAt;
	}

	public static PostCommentResponse from(User user, PostComment postComment) {
		return new PostCommentResponse(
			user.getNickname(),
			user.getImageUrl(),
			postComment.getContent(),
			postComment.getUser().getCreatedAt()
		);
	}

}

