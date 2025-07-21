package com.example.place.domain.user.dto;

import lombok.Getter;

@Getter
public class UserRegisterResponse {
	private final String email;

	public UserRegisterResponse(String email) {
		this.email = email;
	}
}
