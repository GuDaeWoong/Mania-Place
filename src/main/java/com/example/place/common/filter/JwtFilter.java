package com.example.place.common.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.common.security.jwt.JwtUtil;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter implements Ordered {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	private static final String BEARER_PREFIX = "Bearer ";

	@Override
	public int getOrder() {
		return 2;
	}

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

		if (!jwtUtil.validateToken(jwt)) {
			throw new CustomException(ExceptionCode.INVALID_OR_MISSING_TOKEN);
		}

		String subject = jwtUtil.extractUserId(jwt);
		Long userId = Long.parseLong(subject);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

		CustomPrincipal principal = new CustomPrincipal(user.getId(), user.getName(), user.getNickname(), user.getEmail());

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
}
