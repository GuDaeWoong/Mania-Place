// package com.example.place.security.jwt;
//
// import static org.assertj.core.api.Assertions.*;
//
// import java.util.Date;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;
//
// import com.example.place.common.exception.enums.ExceptionCode;
// import com.example.place.common.exception.exceptionclass.CustomException;
// import com.example.place.common.security.jwt.JwtUtil;
//
// import io.jsonwebtoken.Claims;
//
// @SpringBootTest
// @DisplayName("JWT 유틸리티 테스트")
// @ActiveProfiles("test")
// public class JwtUtilTest {
//
// 	private static final Long DEFAULT_USER_ID = 12345L;
//
// 	@Autowired
// 	private JwtUtil jwtUtil;
// 	private String tokenWithBearer;
// 	private String tokenWithoutBearer;
//
// 	@BeforeEach
// 	void setUp() {
// 		tokenWithBearer = jwtUtil.createAccessToken(DEFAULT_USER_ID);
// 		tokenWithoutBearer = tokenWithBearer.substring(7);
// 	}
//
// 	@Test
// 	@DisplayName("액세스 토큰 생성")
// 	void createAccessToken() {
// 		assertThat(tokenWithBearer).isNotEmpty();
// 		assertThat(tokenWithBearer).startsWith("Bearer ");
// 		assertThat(tokenWithoutBearer.split("\\.")).hasSize(3);
// 	}
//
// 	@Test
// 	@DisplayName("Bearer 접두사 제거 성공")
// 	void subStringToken_Success() {
// 		String stripped = jwtUtil.subStringToken(tokenWithBearer);
// 		assertThat(stripped).doesNotContain("Bearer ");
// 		assertThat(stripped.split("\\.")).hasSize(3);
// 	}
//
// 	@Test
// 	@DisplayName("토큰이 없거나 잘못된 형식이면 CustomException 발생")
// 	void subStringToken_InvalidToken() {
// 		String malformedToken = "malformedToken";
//
// 		assertThatThrownBy(() -> jwtUtil.subStringToken(null)).isInstanceOf(CustomException.class)
// 			.hasMessageContaining(ExceptionCode.INVALID_OR_MISSING_TOKEN.getMessage());
//
// 		assertThatThrownBy(() -> jwtUtil.subStringToken(malformedToken)).isInstanceOf(CustomException.class)
// 			.hasMessageContaining(ExceptionCode.INVALID_OR_MISSING_TOKEN.getMessage());
// 	}
//
// 	@Test
// 	@DisplayName("Claims 추출")
// 	void extractClaims() {
// 		Claims claims = jwtUtil.extractClaims(tokenWithoutBearer);
// 		assertThat(claims.getSubject()).isEqualTo(DEFAULT_USER_ID.toString());
// 		assertThat(claims.getIssuedAt()).isNotNull();
// 		assertThat(claims.getExpiration()).isAfter(new Date());
// 	}
// }