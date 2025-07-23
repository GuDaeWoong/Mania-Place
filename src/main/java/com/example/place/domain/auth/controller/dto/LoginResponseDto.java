package com.example.place.domain.auth.controller.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {
	private final String accessToken;

	public LoginResponseDto(String accessToken) {
		this.accessToken = accessToken;
	}
}