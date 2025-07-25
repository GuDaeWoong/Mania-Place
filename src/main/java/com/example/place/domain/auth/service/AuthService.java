package com.example.place.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.common.security.jwt.JwtUtil;
import com.example.place.domain.auth.controller.dto.LoginRequestDto;
import com.example.place.domain.auth.controller.dto.LoginResponseDto;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final JwtBlacklistService jwtBlacklistService;
	private final JwtUtil jwtUtil;

	public LoginResponseDto login(LoginRequestDto requestDto) {
		User user = (User)userService.findByEmailOrElseThrow(requestDto.getEmail())
			.orElseThrow(() -> new CustomException(ExceptionCode.INVALID_EMAIL_OR_PASSWORD));

		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new CustomException(ExceptionCode.PASSWORD_MISMATCH);
		}

		String accessToken = jwtUtil.createAccessToken(user.getId());
		String refreshToken = jwtUtil.createRefreshToken(user.getId());

		return new LoginResponseDto(accessToken, refreshToken);
	}

	@Transactional
	public void logout(String bearerToken, String refreshToken) {

		String accessToken = jwtUtil.subStringToken(bearerToken);
		jwtBlacklistService.addBlacklist(accessToken);

		if (refreshToken != null && !refreshToken.isEmpty()) {
			jwtBlacklistService.addBlacklist(refreshToken);
		}
	}

	@Transactional
	public LoginResponseDto refreshAccessToken(String refreshToken) {

		if (!jwtUtil.validateToken(refreshToken)) {
			throw new CustomException(ExceptionCode.EXPIRED_REFRESH_TOKEN);
		}

		if (jwtBlacklistService.isBlacklisted(refreshToken)) {
			throw new CustomException(ExceptionCode.BLACKLISTED_TOKEN);
		}

		String userIdStr = jwtUtil.extractUserId(refreshToken);

		Long userId = Long.parseLong(userIdStr);

		User user = userService.findByIdOrElseThrow(userId);

		String newAccessToken = jwtUtil.createAccessToken(user.getId());

		return new LoginResponseDto(newAccessToken, refreshToken);
	}
}