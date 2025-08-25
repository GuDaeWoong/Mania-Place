package com.example.place.domain.item.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.example.place.common.entity.SoftDeleteEntity;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.item.dto.request.ItemRequest;
import com.example.place.domain.itemcomment.entity.ItemComment;
import com.example.place.domain.itemtag.entity.ItemTag;
import com.example.place.domain.user.entity.User;

import jakarta.persistence.*;
import lombok.AccessLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends SoftDeleteEntity {
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
	// private Long totalCount;
	private Boolean isLimited;
	private LocalDateTime salesStartAt;
	private LocalDateTime salesEndAt;

	@OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Image> images = new ArrayList<>();

	@OneToMany(mappedBy = "item", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
	private Set<ItemTag> itemTags = new LinkedHashSet<>();

	@OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<ItemComment> comments = new ArrayList<>();

	// 재고 감소
	public void decreaseStock(Long quantity){
		if(this.count < quantity){
			throw new CustomException(ExceptionCode.OUT_OF_STOCK);
		}
		this.count -= quantity;
	}

	// 주문 취소로 인한 재고 증가
	public void increaseStock(Long quantity) {
		this.count += quantity;
	}

	// 한정상품 여부
	public boolean isLimitedEdition() {
		return this.isLimited != null && this.isLimited;
	}

	private Item(User user, String itemName, String itemDescription, Double price, Long count, boolean isLimited, LocalDateTime salesStartAt, LocalDateTime salesEndAt) {
		this.user = user;
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.price = price;
		this.count = count;
		this.isLimited = isLimited;
		this.salesStartAt = salesStartAt;
		this.salesEndAt = salesEndAt;
	}
	public static Item of(User user, String itemName, String itemDescription, Double price, Long count,  boolean isLimited, LocalDateTime salesStartAt, LocalDateTime salesEndAt) {
		return new Item(user, itemName, itemDescription, price, count, isLimited, salesStartAt, salesEndAt);
	}


	public void updateItem(ItemRequest request) {
		if(request.getItemName() != null) {
			this.itemName = request.getItemName();
		}
		if (request.getItemDescription() != null) {
			this.itemDescription = request.getItemDescription();
		}
		if (request.getPrice() != null) {
			this.price = request.getPrice();
		}
		if (request.getCount() != null) {
			this.count = request.getCount();
		}
		if (request.getSalesStartAt() != null) {
			this.salesStartAt = request.getSalesStartAt();
		}
		if (request.getSalesEndAt() != null) {
			this.salesEndAt = request.getSalesEndAt();
		}
	}

	public void validateSalesPeriod() {
		LocalDateTime now = LocalDateTime.now();
		if (now.isBefore(salesStartAt) || now.isAfter(salesEndAt)) {
			throw new CustomException(ExceptionCode.NOT_SALE_PERIOD);
		}
	}

	public void addItemTag(ItemTag itemTag) {
		this.itemTags.add(itemTag);
		itemTag.setItem(this);
	}

	public void addImage(Image image) {
		this.images.add(image);
	}

}
