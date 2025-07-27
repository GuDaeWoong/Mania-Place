package com.example.place.domain.post.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
	@NotNull(message = "수정할 내용을 입력해야합니다")
	private String content;
}