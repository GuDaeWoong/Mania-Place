package com.example.place.domain.itemcomment.dto.response;

import java.time.LocalDateTime;

import com.example.place.domain.itemcomment.entity.ItemComment;

import lombok.Getter;

@Getter
public class ItemCommentResponse {
	private String nickname;
	private String content;
	private LocalDateTime createAt;

	private ItemCommentResponse(String nickname, String content, LocalDateTime createAt) {
		this.nickname = nickname;
		this.content = content;
		this.createAt = createAt;
	}

	public static ItemCommentResponse of(ItemComment itemComment) {
		return new ItemCommentResponse(
			itemComment.getUser().getNickname(),
			itemComment.getContent(),
			itemComment.getCreatedAt());
	}
}
