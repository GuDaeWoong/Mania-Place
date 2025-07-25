package com.example.place.domain.post.dto.response;

import com.example.place.domain.post.entity.Post;

import lombok.Getter;

@Getter
public class PostWithUserResponseDto {
	private final Long id;
	private final String content;
	private final String nickname;
	private final Long itemId;

	public PostWithUserResponseDto(Post post,String nickname) {
		this.id = post.getId();
		this.content = post.getContent();
		this.itemId = post.getItem().getId();
		this.nickname = nickname;
	}
}