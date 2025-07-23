package com.example.place.domain.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
	@Size(min = 2, max = 30, message = "2자 이상 30자 이하로 입력해주세요.")
	private final String name;
	@Size(min = 2, max = 30, message = "2자 이상 30자 이하로 입력해주세요.")
	private final String nickname;
	// private final String tags;
	private final String imageUrl;

	public UserUpdateRequest(String name, String nickname, String imageUrl) {
		this.name = name;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
	}
}
