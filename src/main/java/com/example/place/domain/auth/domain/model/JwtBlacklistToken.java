package com.example.place.domain.auth.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jwt_blacklist")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtBlacklistToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 500)
	private String token;

	@Column(nullable = false)
	private LocalDateTime blacklistedAt;

	public JwtBlacklistToken(String token, LocalDateTime blacklistedAt) {
		this.token = token;
		this.blacklistedAt = blacklistedAt;
	}

	public static JwtBlacklistToken of(String token) {
		return new JwtBlacklistToken(token, LocalDateTime.now());
	}
}