package com.example.place.domain.newsfeedcomment.dto.response;

import java.time.LocalDateTime;

import com.example.place.domain.newsfeedcomment.entity.NewsfeedComment;
import com.example.place.domain.user.entity.User;

import lombok.Getter;

@Getter
public class NewsfeedCommentResponse {

	private String nickname;
	private String userImageUrl;
	private String content;
	private LocalDateTime createAt;

	private NewsfeedCommentResponse(String nickname, String userImageUrl, String content, LocalDateTime createAt) {
		this.nickname = nickname;
		this.userImageUrl = userImageUrl;
		this.content = content;
		this.createAt = createAt;
	}

	public static NewsfeedCommentResponse from(User user, NewsfeedComment newsfeedComment) {
		return new NewsfeedCommentResponse(
			user.getNickname(),
			user.getImageUrl(),
			newsfeedComment.getContent(),
			newsfeedComment.getUser().getCreatedAt()
		);
	}
}
