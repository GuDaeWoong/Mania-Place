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
import com.example.place.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtBlacklistService jwtBlacklistService;
	private final JwtUtil jwtUtil;

	public LoginResponseDto login(LoginRequestDto requestDto) {
		User user = userRepository.findByEmail(requestDto.getEmail())
			.orElseThrow(() -> new CustomException(ExceptionCode.INVALID_EMAIL_OR_PASSWORD));

		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new CustomException(ExceptionCode.PASSWORD_MISMATCH);
		}
		String accessToken = jwtUtil.createAccessToken(user.getId());
		return new LoginResponseDto(accessToken);
	}

	@Transactional
	public void logout(String bearerToken) {

		String accessToken = jwtUtil.subStringToken(bearerToken);

		jwtBlacklistService.addBlacklist(accessToken);
	}
}