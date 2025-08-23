package com.example.place.domain.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.common.security.jwt.JwtUtil;
import com.example.place.domain.auth.controller.dto.LoginRequestDto;
import com.example.place.domain.auth.controller.dto.LoginResponseDto;
import com.example.place.domain.auth.service.AuthService;
import com.example.place.domain.auth.service.JwtBlacklistService;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	private static final String TEST_EMAIL = "test@example.com";
	private static final String TEST_PASSWORD = "password123";
	private static final String ENCODED_PASSWORD = "encodedPassword";

	private static final String ACCESS_TOKEN = "mockAccessToken";
	private static final String REFRESH_TOKEN = "mockRefreshToken";
	private static final String NEW_ACCESS_TOKEN = "newAccessToken";
	private static final String EXPIRED_REFRESH_TOKEN = "expiredRefreshToken";
	private static final String BLACKLISTED_TOKEN = "blacklistedToken";

	@Mock
	private UserService userService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtBlacklistService jwtBlacklistService;

	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private AuthService authService;

	private User testUser;
	private LoginRequestDto loginRequestDto;

	@BeforeEach
	void setUp() {
		testUser = User.of(
			"테스트유저",
			"testUser123",
			TEST_EMAIL,
			ENCODED_PASSWORD,
			null,
			UserRole.USER
		);
		ReflectionTestUtils.setField(testUser, "id", 1L);

		loginRequestDto = new LoginRequestDto(TEST_EMAIL, TEST_PASSWORD);
	}

	@Test
	@DisplayName("로그인 성공")
	void login_success() {
		// Given: 존재하는 유저 + 비밀번호 일치 상황을 Mocking
		when(userService.findByEmailOrElseThrow(TEST_EMAIL)).thenReturn(Optional.ofNullable(testUser));
		when(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
		when(jwtUtil.createAccessToken(testUser.getId())).thenReturn(ACCESS_TOKEN);
		when(jwtUtil.createRefreshToken(testUser.getId())).thenReturn(REFRESH_TOKEN);

		// When: 로그인 시도
		LoginResponseDto responseDto = authService.login(loginRequestDto);

		// Then: 정상적으로 Access/RefreshToken이 발급되는지 검증
		assertNotNull(responseDto);
		assertEquals(ACCESS_TOKEN, responseDto.getAccessToken());
		assertEquals(REFRESH_TOKEN, responseDto.getRefreshToken());

		// 호출 검증: 필요한 의존성 메서드들이 정확히 1번씩 호출되었는지 확인
		verify(userService, times(1)).findByEmailOrElseThrow(TEST_EMAIL);
		verify(passwordEncoder, times(1)).matches(TEST_PASSWORD, ENCODED_PASSWORD);
		verify(jwtUtil, times(1)).createAccessToken(testUser.getId());
		verify(jwtUtil, times(1)).createRefreshToken(testUser.getId());
	}

	@Test
	@DisplayName("로그인 실패 - 존재하지 않는 이메일")
	void login_fail_userNotFound() {
		// Given: 유저 조회 시 CustomException을 던지도록 설정
		when(userService.findByEmailOrElseThrow(TEST_EMAIL))
			.thenThrow(new CustomException(ExceptionCode.INVALID_EMAIL_OR_PASSWORD));

		// When & Then: 로그인 시도 시 INVALID_EMAIL_OR_PASSWORD 예외가 발생하는지 검증
		CustomException exception = assertThrows(CustomException.class, () -> authService.login(loginRequestDto));
		assertEquals(ExceptionCode.INVALID_EMAIL_OR_PASSWORD, exception.getExceptionCode());
	}

	@Test
	@DisplayName("로그인 실패 - 비밀번호 불일치")
	void login_fail_passwordMismatch() {
		// Given: 유저는 존재하지만 비밀번호가 불일치하는 상황
		when(userService.findByEmailOrElseThrow(TEST_EMAIL)).thenReturn(Optional.ofNullable(testUser));
		when(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

		// When & Then: 로그인 시도 시 PASSWORD_MISMATCH 예외가 발생하는지 검증
		CustomException exception = assertThrows(CustomException.class, () -> authService.login(loginRequestDto));
		assertEquals(ExceptionCode.PASSWORD_MISMATCH, exception.getExceptionCode());
	}

	@Test
	@DisplayName("로그아웃 성공 - Access/Refresh 토큰 모두 블랙리스트에 추가")
	void logout_success_withBothTokens() {
		// Given: Bearer 토큰을 파싱하면 AccessToken을 얻을 수 있도록 설정
		String bearerToken = "Bearer " + ACCESS_TOKEN;
		when(jwtUtil.subStringToken(bearerToken)).thenReturn(ACCESS_TOKEN);

		// When: 로그아웃 시도
		authService.logout(bearerToken, REFRESH_TOKEN);

		// Then: AccessToken, RefreshToken 모두 블랙리스트에 등록되는지 검증
		verify(jwtBlacklistService, times(1)).addBlacklist(ACCESS_TOKEN);
		verify(jwtBlacklistService, times(1)).addBlacklist(REFRESH_TOKEN);
	}

	@Test
	@DisplayName("로그아웃 성공 - Access 토큰만 블랙리스트에 추가 (Refresh 토큰이 null)")
	void logout_success_withAccessTokenOnly() {
		// Given
		String bearerToken = "Bearer " + ACCESS_TOKEN;
		when(jwtUtil.subStringToken(bearerToken)).thenReturn(ACCESS_TOKEN);

		// When: 로그아웃 시도 (RefreshToken 없음)
		authService.logout(bearerToken, null);

		// Then: AccessToken만 블랙리스트에 추가되고, null 값은 무시되는지 검증
		verify(jwtBlacklistService, times(1)).addBlacklist(ACCESS_TOKEN);
		verify(jwtBlacklistService, never()).addBlacklist(null);
	}

	@Test
	@DisplayName("토큰 재발급 성공")
	void refreshAccessToken_success() {
		// Given: 유효한 RefreshToken 상황
		when(jwtUtil.validateToken(REFRESH_TOKEN)).thenReturn(true);
		when(jwtBlacklistService.isBlacklisted(REFRESH_TOKEN)).thenReturn(false);
		when(jwtUtil.extractUserId(REFRESH_TOKEN)).thenReturn(String.valueOf(testUser.getId()));
		when(userService.findByIdOrElseThrow(testUser.getId())).thenReturn(testUser);
		when(jwtUtil.createAccessToken(testUser.getId())).thenReturn(NEW_ACCESS_TOKEN);

		// When: AccessToken 재발급 요청
		LoginResponseDto responseDto = authService.refreshAccessToken(REFRESH_TOKEN);

		// Then: 새로운 AccessToken 발급, 기존 RefreshToken은 그대로 반환되는지 검증
		assertNotNull(responseDto);
		assertEquals(NEW_ACCESS_TOKEN, responseDto.getAccessToken());
		assertEquals(REFRESH_TOKEN, responseDto.getRefreshToken());
	}

	@Test
	@DisplayName("토큰 재발급 실패 - 만료된 Refresh 토큰")
	void refreshAccessToken_fail_expiredToken() {
		// Given: validateToken이 false → 만료된 RefreshToken 상황
		when(jwtUtil.validateToken(EXPIRED_REFRESH_TOKEN)).thenReturn(false);

		// When & Then: 재발급 시도 시 EXPIRED_REFRESH_TOKEN 예외 발생 검증
		CustomException exception = assertThrows(CustomException.class,
			() -> authService.refreshAccessToken(EXPIRED_REFRESH_TOKEN));
		assertEquals(ExceptionCode.EXPIRED_REFRESH_TOKEN, exception.getExceptionCode());
	}

	@Test
	@DisplayName("토큰 재발급 실패 - 블랙리스트에 등록된 Refresh 토큰")
	void refreshAccessToken_fail_blacklistedToken() {
		// Given: 토큰 자체는 유효하지만 블랙리스트에 등록된 상황
		when(jwtUtil.validateToken(BLACKLISTED_TOKEN)).thenReturn(true);
		when(jwtBlacklistService.isBlacklisted(BLACKLISTED_TOKEN)).thenReturn(true);

		// When & Then: 재발급 시도 시 BLACKLISTED_TOKEN 예외 발생 검증
		CustomException exception = assertThrows(CustomException.class,
			() -> authService.refreshAccessToken(BLACKLISTED_TOKEN));
		assertEquals(ExceptionCode.BLACKLISTED_TOKEN, exception.getExceptionCode());
	}
}