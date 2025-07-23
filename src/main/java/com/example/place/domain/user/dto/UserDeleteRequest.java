package com.example.place.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserDeleteRequest {

	@NotBlank
	private final String password;

	public UserDeleteRequest(String password) {
		this.password = password;
	}
}
