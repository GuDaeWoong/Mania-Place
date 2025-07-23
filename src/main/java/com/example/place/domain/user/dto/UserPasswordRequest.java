package com.example.place.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserPasswordRequest {

	@NotBlank
	private final String oldPassword;

	@NotBlank
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*?[#?!@$ %^&*-]).{8,}$", message = "비밀번호는 8자 이상이어야 하고, 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다.")
	private final String newPassword;

	@NotBlank
	private final String newPasswordCheck;

	public UserPasswordRequest(String oldPassword, String newPassword, String newPasswordCheck) {
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.newPasswordCheck = newPasswordCheck;
	}
}
