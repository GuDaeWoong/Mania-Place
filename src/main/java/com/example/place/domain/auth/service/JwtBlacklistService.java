package com.example.place.domain.auth.service;

import org.springframework.stereotype.Service;

import com.example.place.domain.auth.domain.model.JwtBlacklistToken;
import com.example.place.domain.auth.domain.repository.JwtBlacklistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

	private final JwtBlacklistRepository jwtBlacklistRepository;

	public void addBlacklist(String token) {
		JwtBlacklistToken blacklistToken = JwtBlacklistToken.of(token);
		jwtBlacklistRepository.save(blacklistToken);
	}

	public boolean isBlacklisted(String token) {
		return jwtBlacklistRepository.existsByToken(token);
	}
}