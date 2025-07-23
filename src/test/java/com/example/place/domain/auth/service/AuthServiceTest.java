package com.example.place.domain.auth.service;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.common.security.jwt.JwtUtil;
import com.example.place.domain.auth.controller.dto.LoginRequestDto;
import com.example.place.domain.auth.controller.dto.LoginResponseDto;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.repository.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Service:Auth")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	private static final String TEST_EMAIL = "test@example.com";
	private static final String TEST_RAW_PASSWORD = "RAW_Test1234!";
	private static final String TEST_ENCODED_PASSWORD = "ENCODED_Test1234!";
	private static final String TEST_ACCESS_TOKEN = "test.access.token";
	private static final User TEST_USER = User.of(
		"testUser",
		"testNickname",
		TEST_EMAIL, TEST_ENCODED_PASSWORD,
		null,
		UserRole.USER
	);

	private static final LoginRequestDto TEST_LOGIN_REQUEST = new LoginRequestDto(TEST_EMAIL, TEST_RAW_PASSWORD);

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private AuthService authService;

	@Test
	@DisplayName("로그인 성공")
	void loginSuccess() {

		// given
		when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(TEST_USER));
		when(passwordEncoder.matches(TEST_RAW_PASSWORD, TEST_ENCODED_PASSWORD)).thenReturn(true);
		when(jwtUtil.createAccessToken(any())).thenReturn(TEST_ACCESS_TOKEN);

		// when
		LoginResponseDto response = authService.login(TEST_LOGIN_REQUEST);

		// then
		assertThat(response.getAccessToken()).isEqualTo(TEST_ACCESS_TOKEN);
		verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
		verify(passwordEncoder, times(1)).matches(TEST_RAW_PASSWORD, TEST_ENCODED_PASSWORD);
		verify(jwtUtil, times(1)).createAccessToken(any());
	}

	@Test
	@DisplayName("로그인 실패 - 존재하지 않는 이메일")
	void loginFail_userNotFound() {
		// given
		when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

		// when & then
		CustomException exception = assertThrows(CustomException.class, () -> authService.login(TEST_LOGIN_REQUEST));

		assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.INVALID_EMAIL_OR_PASSWORD);
		verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
		verify(passwordEncoder, never()).matches(anyString(), anyString());
		verify(jwtUtil, never()).createAccessToken(any(Long.class));
	}

	@Test
	@DisplayName("로그인 실패 - 비밀번호 불일치")
	void loginFail_passwordMismatch() {
		// given
		when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(TEST_USER));
		when(passwordEncoder.matches(TEST_RAW_PASSWORD, TEST_ENCODED_PASSWORD)).thenReturn(false);

		// when & then
		CustomException exception = assertThrows(CustomException.class, () -> authService.login(TEST_LOGIN_REQUEST));

		assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.PASSWORD_MISMATCH);
		verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
		verify(passwordEncoder, times(1)).matches(TEST_RAW_PASSWORD, TEST_ENCODED_PASSWORD);
		verify(jwtUtil, never()).createAccessToken(any());
	}
}