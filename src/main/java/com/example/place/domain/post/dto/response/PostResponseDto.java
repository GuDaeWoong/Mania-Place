package com.example.place.domain.post.dto.response;

import com.example.place.domain.post.entity.Post;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {
	private Long id;
	private String content;
	private String image;
	private Long userId;
	private Long itemId;

	public PostResponseDto(Post post) {
		this.id = post.getId();
		this.content = post.getContent();
		this.image = post.getImage();
		this.userId = post.getUser().getId();
		this.itemId = post.getItem().getId();
	}

}
