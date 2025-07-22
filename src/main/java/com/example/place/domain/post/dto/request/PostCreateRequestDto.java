package com.example.place.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PostCreateRequestDto {

	@NotNull
	private Long userId;

	@NotNull
	private Long itemId;

	@NotBlank
	private String content;

	private String image;
}