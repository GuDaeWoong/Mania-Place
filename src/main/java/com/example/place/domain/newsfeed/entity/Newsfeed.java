package com.example.place.domain.newsfeed.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.place.common.entity.SoftDeleteEntity;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.user.entity.User;

import jakarta.persistence.*;
import lombok.AccessLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "newsfeeds")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Newsfeed extends SoftDeleteEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private String title;
	private String content;

	@OneToMany(mappedBy = "newsfeed", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Image> images = new ArrayList<>();

	// newsfeed comment
	// @OneToMany(mappedBy = "newsfeed", cascade = CascadeType.REMOVE, orphanRemoval = true)
	// private List<newsfeedComment> comments = new ArrayList<>();

	private Newsfeed(User user, String title, String content) {
		this.user = user;
		this.title = title;
		this.content = content;
	}

	public static Newsfeed of(User user, String title, String content) {
		return new Newsfeed(user, title, content);
	}

	public void addImage(Image image) {
		this.images.add(image);
	}
}
