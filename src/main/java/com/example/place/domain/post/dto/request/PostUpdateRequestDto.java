package com.example.place.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
	@NotBlank(message = "수정할 내용을 입력해야합니다")
	private String content;
}