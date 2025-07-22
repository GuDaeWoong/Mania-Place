package com.example.place.domain.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.place.domain.auth.domain.model.JwtBlacklistToken;

public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklistToken,Long> {
	boolean existsByToken(String token);
}