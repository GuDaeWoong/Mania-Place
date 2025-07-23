package com.example.place.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.place.domain.auth.controller.dto.LoginRequestDto;
import com.example.place.domain.auth.controller.dto.LoginResponseDto;
import com.example.place.domain.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Controller:Auth")
class AuthControllerTest {

	private static final String ENDPOINT = "/api/login";
	private static final String TEST_EMAIL = "test@example.com";
	private static final String TEST_PASSWORD = "password123";
	private static final String TEST_ACCESS_TOKEN = "test.access.token";
	@MockitoBean
	private AuthService authService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("[200:OK] 로그인 성공 API")
	void loginApi_Success() throws Exception {
		// given
		LoginRequestDto loginRequest = createLoginRequest(TEST_EMAIL, TEST_PASSWORD);
		LoginResponseDto loginResponse = new LoginResponseDto(TEST_ACCESS_TOKEN);

		when(authService.login(any(LoginRequestDto.class))).thenReturn(loginResponse);

		// when
		ResultActions resultActions = performPostLogin(loginRequest);

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(header().exists(AUTHORIZATION))
			.andExpect(header().string(AUTHORIZATION, TEST_ACCESS_TOKEN))
			.andExpect(jsonPath("message").value("로그인에 성공했습니다."))
			.andExpect(jsonPath("data.accessToken").value(TEST_ACCESS_TOKEN))
			.andDo(print());
	}

	@Test
	@DisplayName("[400:BAD_REQUEST] 로그인 실패 - 입력값이 잘못됨")
	void loginApi_Fails() throws Exception {
		// given
		LoginRequestDto emptyInputRequest = createLoginRequest("", "");

		// when
		ResultActions result = performPostLogin(emptyInputRequest);

		// then
		result
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	private LoginRequestDto createLoginRequest(String email, String password) {
		return new LoginRequestDto(email, password);
	}

	private ResultActions performPostLogin(Object requestBody) throws Exception {
		return mockMvc.perform(post(ENDPOINT)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestBody))
		);
	}
}