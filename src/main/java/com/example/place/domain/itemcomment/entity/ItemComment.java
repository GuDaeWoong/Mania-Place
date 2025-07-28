package com.example.place.domain.itemcomment.entity;

import com.example.place.common.entity.BaseEntity;
import com.example.place.common.entity.SoftDeleteEntity;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "itemcomments")
public class ItemComment extends SoftDeleteEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	private String content;

	private ItemComment(User user, Item item, String content) {
		this.user = user;
		this.item = item;
		this.content = content;
	}

	public static ItemComment of(User user, Item item, String content) {
		return new ItemComment(user, item, content);
	}

	public void updateItemComment(String content) {
		this.content = content;
	}
}
