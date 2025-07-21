package com.example.place.common.security.jwt;

import lombok.Getter;

@Getter
public class CustomPrincipal {

	private final Long id;
	private final String name;
	private final String nickname;
	private final String email;

	public CustomPrincipal(Long id, String name, String nickname, String email) {
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.email = email;
	}
}