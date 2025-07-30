package com.example.place.domain.Image.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import com.example.place.domain.item.entity.Item;
import com.example.place.domain.newsfeed.entity.Newsfeed;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "images")
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "newsfeed_id")
	private Newsfeed newsfeed;

	private String imageUrl;

	private boolean isMain = false;

	private Image(Item item, String imageUrl, Boolean isMain) {
		if (item == null) {
			throw new IllegalArgumentException("Item cannot be null");
		}
		this.item = item;
		this.imageUrl = imageUrl;
		this.isMain = isMain;
	}

	private Image(Newsfeed newsfeed, String imageUrl, Boolean isMain) {
		if (newsfeed == null) {
			throw new IllegalArgumentException("Newsfeed cannot be null");
		}
		this.newsfeed = newsfeed;
		this.imageUrl = imageUrl;
		this.isMain = isMain;
	}

	public static Image of(Item item, String imageUrl, Boolean isMain) {
		return new Image(item, imageUrl, isMain);
	}

	public static Image of(Newsfeed newsfeed, String imageUrl, Boolean isMain) {
		return new Image(newsfeed, imageUrl, isMain);
	}

	public void updateIsMain(boolean isMain) {
		this.isMain = isMain;
	}
}
