package com.example.place.domain.postcomment.dto;

import java.time.LocalDateTime;



import lombok.Getter;

@Getter
public class PostCommentResponse {
	private String nickname;
	private String userImageUrl;
	private String content;
	private LocalDateTime createAt;

	public PostCommentResponse(String nickname, String userImageUrl, String content, LocalDateTime createAt) {
		this.nickname = nickname;
		this.userImageUrl = userImageUrl;
		this.content = content;
		this.createAt = createAt;
	}

}

