package com.example.place.domain.Image.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.newsfeed.entity.NewsFeed;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "images")
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "newsfeed_id")
	// private NewsFeed newsFeed;

	private String imageUrl;

	private Boolean isMain = false;

	private Image(Item item, String imageUrl, Boolean isMain) {
		this.item = item;
		this.imageUrl = imageUrl;
		this.isMain = isMain;
	}

	public static Image of(Item item, String imageUrl, Boolean isMain) {
		return new Image(item, imageUrl, isMain);
	}
}
