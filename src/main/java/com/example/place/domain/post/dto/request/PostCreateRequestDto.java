package com.example.place.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostCreateRequestDto {

	private Long userId;
	private Long itemId;

	@NotBlank(message = "내용을 입력해주세요.")
	private String content;

	private String image;
}