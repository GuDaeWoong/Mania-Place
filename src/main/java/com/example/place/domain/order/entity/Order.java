package com.example.place.domain.order.entity;

import java.time.LocalDateTime;

import com.example.place.common.entity.BaseEntity;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	//상품 개수
	private int quantity;
	private double price;
	private String deliveryAddress;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	private LocalDateTime completeAt;

	public Order(User user, Item item, int quantity, double price, OrderStatus status,
		String deliveryAddress, LocalDateTime completeAt) {
		this.user = user;
		this.item = item;
		this.quantity = quantity;
		this.price = price;
		this.status = status;
		this.deliveryAddress = deliveryAddress;
		this.completeAt = completeAt;
	}
}
