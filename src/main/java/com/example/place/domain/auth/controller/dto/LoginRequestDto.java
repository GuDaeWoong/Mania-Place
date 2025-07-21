package com.example.place.domain.auth.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {
	@Email
	@NotBlank
	private final String email;
	@NotBlank
	private final String password;

	public LoginRequestDto(String email,String password) {
		this.email = email;
		this.password = password;
	}
}