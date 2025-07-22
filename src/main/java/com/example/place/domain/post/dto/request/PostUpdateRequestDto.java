package com.example.place.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;

public class PostUpdateRequestDto {
	@NotBlank
	private String content;

	private String image;
}
