package com.example.place.domain.user.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.place.common.entity.SoftDeleteEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User extends SoftDeleteEntity {
	private static final String DEFAULT_IMAGE_URL = "/images/default-profile.png";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String nickname;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;

	private String imageUrl;
	@Enumerated(EnumType.STRING)
	private UserRole role;
	//private String tags;

	public User(String name, String nickname, String email, String password, String imageUrl,
		UserRole role) {
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.imageUrl = imageUrl != null ? imageUrl : DEFAULT_IMAGE_URL;
		this.role = role;
	}

	public User() {

	}
}
