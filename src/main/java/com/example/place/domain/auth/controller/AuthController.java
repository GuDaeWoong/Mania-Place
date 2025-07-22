package com.example.place.domain.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.domain.auth.controller.dto.LoginRequestDto;
import com.example.place.domain.auth.controller.dto.LoginResponseDto;
import com.example.place.domain.auth.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponseDto<LoginResponseDto>> loginApi(@Valid @RequestBody LoginRequestDto requestDto,
		HttpServletResponse response) {
		LoginResponseDto loginResponse = authService.login(requestDto);
		response.addHeader(HttpHeaders.AUTHORIZATION, loginResponse.getAccessToken());
		return ResponseEntity.ok(new ApiResponseDto<>("로그인에 성공했습니다.", loginResponse));
	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponseDto<String>> logout(
		@RequestHeader(value = "Authorization", required = false) String bearerToken) {

		authService.logout(bearerToken);

		return ResponseEntity.ok(new ApiResponseDto<>("로그아웃 성공",null));
	}
}