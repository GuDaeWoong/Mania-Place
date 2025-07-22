package com.example.place.domain.post.dto.request;

import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
	private String title;
	private String content;
	private String image;
}

