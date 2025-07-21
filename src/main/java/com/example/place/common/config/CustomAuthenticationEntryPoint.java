package com.example.place.common.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.place.common.dto.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException
	) throws IOException {
		HttpStatus status = HttpStatus.UNAUTHORIZED;

		ErrorResponseDto errorResponseDto = new ErrorResponseDto(
			status.value(),
			status.getReasonPhrase(),
			"인증되지 않은 URL 요청입니다.",
			request.getRequestURI()
		);
		String responseBody = objectMapper.writeValueAsString(errorResponseDto);

		response.setStatus(status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter().write(responseBody);
	}
}