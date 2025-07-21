package com.example.place.security.fixture;

import java.util.Base64;

import org.springframework.test.util.ReflectionTestUtils;

import com.example.place.common.security.jwt.JwtUtil;

public class JwtUtilFixture {

	public static final Long DEFAULT_USER_ID = 12345L;
	public static final String SECRET_KEY = Base64.getEncoder()
		.encodeToString("TestSecretKey01234567890123456789012".getBytes());

	public final JwtUtil jwtUtil;
	public final String tokenWithBearer;
	public final String tokenWithoutBearer;

	public JwtUtilFixture() {
		jwtUtil = new JwtUtil();
		ReflectionTestUtils.setField(jwtUtil, "secretKey", SECRET_KEY);
		jwtUtil.init();

		tokenWithBearer = jwtUtil.createAccessToken(DEFAULT_USER_ID);
		tokenWithoutBearer = tokenWithBearer.substring(7);
	}
}