package com.example.place.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.place.common.entity.SoftDeleteEntity;
import com.example.place.domain.usertag.entity.UserTag;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends SoftDeleteEntity {
	private static final String DEFAULT_IMAGE_URL = "/images/default-profile.png";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false, unique = true)
	private String nickname;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String password;

	private String imageUrl;
	@Enumerated(EnumType.STRING)
	private UserRole role;

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<UserTag> userTags = new ArrayList<>();


	private User(String name, String nickname, String email, String password, String imageUrl,
		UserRole role) {
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.imageUrl = imageUrl != null ? imageUrl : DEFAULT_IMAGE_URL;
		this.role = role;
	}

	public void updateUserInfo(String name, String nickname, String imageUrl) {
		this.name = name;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
	}

	public void updateUserPassword(String password) {
		this.password = password;
	}

	public static User of(String name,String nickname,String email,String password , String imageUrl,UserRole role){
		return new User(name,nickname,email,password,imageUrl,role);
	}
}