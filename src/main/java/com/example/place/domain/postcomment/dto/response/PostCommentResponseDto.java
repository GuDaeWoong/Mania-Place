package com.example.place.domain.postcomment.dto.response;

import java.time.LocalDateTime;


import com.example.place.domain.postcomment.entity.PostComment;
import com.example.place.domain.user.entity.User;

import lombok.Getter;

@Getter
public class PostCommentResponseDto {
	private String nickname;
	private String userImageUrl;
	private String content;
	private LocalDateTime createAt;

	private PostCommentResponseDto(String nickname, String userImageUrl, String content, LocalDateTime createAt) {
		this.nickname = nickname;
		this.userImageUrl = userImageUrl;
		this.content = content;
		this.createAt = createAt;
	}

	public static PostCommentResponseDto from(User user, PostComment postComment) {
		return new PostCommentResponseDto(
			user.getNickname(),
			user.getImageUrl(),
			postComment.getContent(),
			postComment.getUser().getCreatedAt()
		);
	}

}

