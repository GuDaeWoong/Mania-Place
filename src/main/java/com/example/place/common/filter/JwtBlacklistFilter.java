package com.example.place.common.filter;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.place.domain.auth.service.JwtBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtBlacklistFilter extends OncePerRequestFilter implements Ordered {

	private final JwtBlacklistService jwtBlacklistService;

	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String bearerToken = request.getHeader("Authorization");

		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			String token = bearerToken.substring(7);
			if (jwtBlacklistService.isBlacklisted(token)) {
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write("{\"status\":401,\"message\":\"이미 로그아웃된 토큰입니다. 다시 로그인해주세요.\"}");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}
}