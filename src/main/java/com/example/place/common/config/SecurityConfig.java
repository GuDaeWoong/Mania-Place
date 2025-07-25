package com.example.place.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.place.common.filter.JwtBlacklistFilter;
import com.example.place.common.filter.JwtFilter;
import com.example.place.common.security.jwt.JwtUtil;
import com.example.place.domain.auth.service.JwtBlacklistService;
import com.example.place.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class    SecurityConfig {

	private final JwtUtil jwtUtil;
	private final ObjectMapper objectMapper;
	private final UserService userService;
	private final JwtBlacklistService jwtBlacklistService;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtBlacklistFilter jwtBlacklistFilter() {
		return new JwtBlacklistFilter(jwtBlacklistService, objectMapper);
	}

	@Bean
	public JwtFilter jwtFilter() {
		return new JwtFilter(jwtUtil, userService);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)

			.addFilterBefore(jwtBlacklistFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtFilter(), JwtBlacklistFilter.class)

			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/login", "/api/accounts").permitAll()
				.requestMatchers("/error", "/refresh").permitAll()
				.requestMatchers("/api/accounts/admin").hasRole("ADMIN")
				.requestMatchers("/api/admin/**").hasRole("ADMIN")
				.requestMatchers("/api/**").hasAnyRole("USER", "ADMIN")
				.anyRequest().authenticated()
			)

			.exceptionHandling(configurer -> configurer
				.authenticationEntryPoint(customAuthenticationEntryPoint)
			)
			.build();
	}
}