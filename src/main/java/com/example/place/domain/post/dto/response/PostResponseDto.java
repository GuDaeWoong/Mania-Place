package com.example.place.domain.post.dto.response;

import com.example.place.domain.post.entity.Post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {
	private Long id;
	private String content;
	private String image;
	private Long userId;
	private Long itemId;

	public static PostResponseDto from(Post post) {
		PostResponseDto dto = new PostResponseDto();
		dto.setId(post.getId());
		dto.setContent(post.getContent());
		dto.setImage(post.getImage());
		dto.setUserId(post.getUser().getId());
		dto.setItemId(post.getItem().getId());
		return dto;
	}

}
