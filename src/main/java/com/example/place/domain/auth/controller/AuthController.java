package com.example.place.domain.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
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

		ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", loginResponse.getRefreshToken())
			.httpOnly(true)
			// .secure(true)
			.secure(false)
			.path("/")
			.maxAge(7 * 24 * 60 * 60)
			// .sameSite("Strict")
			.sameSite("Lax")
			.build();

		response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
		return ResponseEntity.ok(ApiResponseDto.of("로그인에 성공했습니다.", loginResponse));
	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponseDto<String>> logout(
		@RequestHeader(value = "Authorization", required = false) String bearerToken,
		@CookieValue(value = "refreshToken", required = false) String refreshToken,
		HttpServletResponse response) {

		authService.logout(bearerToken, refreshToken);

		ResponseCookie deleteRefreshTokenCookie = ResponseCookie.from("refreshToken", "")
			.path("/")
			.maxAge(0)
			.httpOnly(true)
			.secure(true)
			.build();
		response.addHeader(HttpHeaders.SET_COOKIE, deleteRefreshTokenCookie.toString());

		return ResponseEntity.ok(ApiResponseDto.of("로그아웃 성공", null));
	}

	@PostMapping("/refresh")
	public ResponseEntity<ApiResponseDto<LoginResponseDto>> refreshAccessToken(
		@CookieValue(name = "refreshToken", required = false) String refreshToken,
		HttpServletResponse response) {

		if (refreshToken == null) {
			throw new CustomException(ExceptionCode.INVALID_REFRESH_TOKEN);
		}

		LoginResponseDto newTokens = authService.refreshAccessToken(refreshToken);

		response.setHeader(HttpHeaders.AUTHORIZATION, newTokens.getAccessToken());

		return ResponseEntity.ok(ApiResponseDto.of("토큰 재발급 성공", newTokens));
	}
}

