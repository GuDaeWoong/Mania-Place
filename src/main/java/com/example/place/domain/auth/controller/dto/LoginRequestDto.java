package com.example.place.domain.auth.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {

	@Email(message = "올바른 이메일 형식이 아닙니다.")
	@NotBlank(message = "이메일을 입력해주세요.")
	private final String email;
	@NotBlank(message = "비밀번호를 입력해주세요.")
	private final String password;

	public LoginRequestDto(String email,String password) {
		this.email = email;
		this.password = password;
	}
}