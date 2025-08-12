package com.example.place.common.security.jwt;

import java.security.Principal;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
public class CustomPrincipal implements UserDetails, Principal {

	private final Long id;
	private final String name;
	private final String nickname;
	private final String email;
	private final Collection<? extends GrantedAuthority> authorities;

	public CustomPrincipal(Long id, String name, String nickname, String email,
		Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}