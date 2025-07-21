package com.example.place.security.jwt;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.security.fixture.JwtUtilFixture;

import io.jsonwebtoken.Claims;

@ExtendWith(MockitoExtension.class)
@DisplayName("JWT 유틸리티 테스트")
public class JwtUtilTest {

	private JwtUtilFixture fixture;

	@BeforeEach
	void setUp() {
		fixture = new JwtUtilFixture();
	}

	@Test
	@DisplayName("액세스 토큰 생성")
	void createAccessToken() {

		//then
		assertThat(fixture.tokenWithBearer).isNotEmpty();
		assertThat(fixture.tokenWithBearer).startsWith("Bearer ");
		assertThat(fixture.tokenWithoutBearer.split("\\.")).hasSize(3);
	}

	@Test
	@DisplayName("Bearer 접두사 제거 성공")
	void subStringToken_Success() {

		// when
		String stripped = fixture.jwtUtil.subStringToken(fixture.tokenWithBearer);

		// then
		assertThat(stripped).doesNotContain("Bearer ");
		assertThat(stripped.split("\\.")).hasSize(3);
	}

	@Test
	@DisplayName("토큰이 없거나 잘못된 형식이면 CustomException 발생")
	void subStringToken_InvalidToken() {
		// given
		String malformedToken = "malformedToken";

		// when & then
		assertThatThrownBy(() -> fixture.jwtUtil.subStringToken(null)).isInstanceOf(CustomException.class)
			.hasMessageContaining(ExceptionCode.INVALID_OR_MISSING_TOKEN.getMessage());

		assertThatThrownBy(() -> fixture.jwtUtil.subStringToken(malformedToken)).isInstanceOf(CustomException.class)
			.hasMessageContaining(ExceptionCode.INVALID_OR_MISSING_TOKEN.getMessage());
	}

	@Test
	@DisplayName("Claims 추출")
	void extractClaims() {

		//when
		Claims claims = fixture.jwtUtil.extractClaims(fixture.tokenWithoutBearer);

		//then
		assertThat(claims.getSubject()).isEqualTo(JwtUtilFixture.DEFAULT_USER_ID.toString());
		assertThat(claims.getIssuedAt()).isNotNull();
		assertThat(claims.getExpiration()).isAfter(new Date());
	}

	@Test
	@DisplayName("토큰 유효성 검사")
	void validateToken() {

		//when
		boolean isValid = fixture.jwtUtil.validateToken(fixture.tokenWithoutBearer);

		//then
		assertThat(isValid).isTrue();
	}

	@Test
	@DisplayName("JWT 토큰에서 유저 ID 추출")
	void getUserId() {

		//when
		String extractedUserId = fixture.jwtUtil.extractUserId(fixture.tokenWithoutBearer);

		//then
		assertThat(extractedUserId).isEqualTo(JwtUtilFixture.DEFAULT_USER_ID.toString());
	}
}