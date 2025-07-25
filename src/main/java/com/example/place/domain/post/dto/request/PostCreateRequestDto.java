package com.example.place.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostCreateRequestDto {

	@NotNull(message = "아이템 ID는 필수입니다.")
	private Long itemId;

	@NotBlank(message = "내용을 입력해주세요.")
	private String content;
}