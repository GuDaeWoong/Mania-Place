package com.example.place.common.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.common.security.jwt.JwtUtil;
import com.example.place.domain.auth.service.JwtBlacklistService;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final JwtBlacklistService jwtBlacklistService;
	private static final String BEARER_PREFIX ="Bearer ";

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		String bearerJwt = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (bearerJwt == null || !bearerJwt.startsWith(BEARER_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}

		String jwt = jwtUtil.subStringToken(bearerJwt);

		if (jwtBlacklistService.isBlacklisted(jwt)) {
			response.setStatus(409);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(
				"{\"status\":409,\"error\":\"Conflict\",\"message\":\"다시 로그인 해주세요.\",\"path\":\""
					+ request.getRequestURI() + "\"}"
			);
			return;
		}

		if (!jwtUtil.validateToken(jwt)) {
			throw new CustomException(ExceptionCode.INVALID_OR_MISSING_TOKEN);
		}

		String subject = jwtUtil.extractUserId(jwt);
		Long userId = Long.parseLong(subject);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

		CustomPrincipal principal = new CustomPrincipal(user.getId(), user.getName(),user.getNickname(),user.getEmail());

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
}