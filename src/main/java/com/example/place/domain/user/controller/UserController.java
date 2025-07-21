package com.example.place.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.domain.user.dto.UserRegisterRequest;
import com.example.place.domain.user.dto.UserRegisterResponse;
import com.example.place.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	//유저 회원가입
	@PostMapping("/accounts")
	public ResponseEntity<ApiResponseDto<UserRegisterResponse>> register(
		@Valid @RequestBody UserRegisterRequest userRegisterRequest
	) {
		return ResponseEntity.ok(new ApiResponseDto<>("회원가입이 완료되었습니다.",userService.register(userRegisterRequest)));
	}
}
