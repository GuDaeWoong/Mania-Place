package com.example.place.domain.post.dto;

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
}
