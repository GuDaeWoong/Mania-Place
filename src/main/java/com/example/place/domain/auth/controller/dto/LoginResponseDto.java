package com.example.place.domain.auth.controller.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {
	private final String accessToken;
	private final String refreshToken;

	public LoginResponseDto(String accessToken,String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}