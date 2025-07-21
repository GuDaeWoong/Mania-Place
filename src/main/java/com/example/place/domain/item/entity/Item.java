package com.example.place.domain.item.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.place.common.entity.BaseEntity;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.itemtag.entity.ItemTag;
import com.example.place.domain.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private String itemName;
	private String itemDescription;

	private double price;
	private Long count;
	private LocalDateTime salesStartAt;
	private LocalDateTime salesEndAt;

	@OneToMany(mappedBy = "item")
	private List<Image> images = new ArrayList<>();

	// 재고 감소
	public void decreaseStock(int quantity){
		if(this.count < quantity){
			throw new CustomException(ExceptionCode.OUT_OF_STOCK);
		}
		this.count -= quantity;
	}

	// 주문 취소로 인한 재고 증가
	public void increaseStock(int quantity) {
		this.count += quantity;
	}


	@OneToMany(mappedBy = "item")
	private List<ItemTag> itemTags = new ArrayList<>();

	/**
	 * security 구현후 수정필요
	 */
	public Item(User user, String itemName, String itemDescription, Double price, Long count, LocalDateTime salesStartAt, LocalDateTime salesEndAt) {
		this.user = user;
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.price = price;
		this.count = count;
		this.salesStartAt = salesStartAt;
		this.salesEndAt = salesEndAt;
	}

}
