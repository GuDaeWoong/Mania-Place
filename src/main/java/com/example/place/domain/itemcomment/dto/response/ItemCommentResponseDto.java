package com.example.place.domain.itemcomment.dto.response;

import java.time.LocalDateTime;

import com.example.place.domain.itemcomment.entity.ItemComment;

import lombok.Getter;

@Getter
public class ItemCommentResponseDto {
	private String nickname;
	private String content;
	private LocalDateTime createAt;

	private ItemCommentResponseDto(String nickname, String content, LocalDateTime createAt) {
		this.nickname = nickname;
		this.content = content;
		this.createAt = createAt;
	}

	public static ItemCommentResponseDto of(ItemComment itemComment) {
		return new ItemCommentResponseDto(itemComment.getUser().getNickname(), itemComment.getContent(),
			itemComment.getCreatedAt());
	}
}
