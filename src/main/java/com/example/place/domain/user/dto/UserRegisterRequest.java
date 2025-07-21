package com.example.place.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserRegisterRequest {
	@NotBlank
	@Size(min = 2, max = 30, message = "2자 이상 30자 이하로 입력해주세요.")
	private final String name;

	@NotBlank
	@Size(min = 2, max = 30, message = "2자 이 30자 이하로 입력해주세요.")
	private final String nickname;

	@NotBlank
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private final String email;

	@NotBlank
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*?[#?!@$ %^&*-]).{8,}$", message = "비밀번호는 8자 이상이어야 하고, 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다.")
	private final String password;

	private final String imageUrl;

	// private final String tags;

	public UserRegisterRequest(String name, String nickname, String email, String password, String imageUrl) {
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.imageUrl = imageUrl;
	}
}
