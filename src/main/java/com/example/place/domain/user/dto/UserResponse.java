package com.example.place.domain.user.dto;

import lombok.Getter;

@Getter
public class UserResponse {
	private final Long id;
	private final String name;
	private final String nickname;
	private final String imageUrl;
	private final String email;

	public UserResponse(Long id, String name, String nickname, String imageUrl, String email) {
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
		this.email = email;
	}
}
