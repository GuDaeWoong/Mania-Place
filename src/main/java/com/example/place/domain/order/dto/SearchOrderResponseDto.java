package com.example.place.domain.order.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class SearchOrderResponseDto {
	private String userNickname;
	private String itemName;
	private int quantity;
	private double price;
	private String status;
	private String deliveryAddress;
	private LocalDateTime createdAt;

	public SearchOrderResponseDto(String nickname, String itemName, int quantity, double price, String deliveryAddress, String status, LocalDateTime createdAt) {
		this.userNickname = nickname;
		this.itemName = itemName;
		this.quantity = quantity;
		this.price = price;
		this.status = status;
		this.deliveryAddress = deliveryAddress;
		this.createdAt = createdAt;

	}
}
