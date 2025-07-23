package com.example.place.domain.user.dto;

import com.example.place.domain.user.entity.User;

import lombok.Getter;

@Getter
public class UserResponse {
	private final Long id;
	private final String name;
	private final String nickname;
	private final String imageUrl;
	private final String email;

	private UserResponse(Long id, String name, String nickname, String imageUrl, String email) {
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
		this.email = email;
	}

	public static UserResponse from(User user) {
		return new UserResponse(
			user.getId(),
			user.getName(),
			user.getNickname(),
			user.getImageUrl(),
			user.getEmail()
		);
	}
}
