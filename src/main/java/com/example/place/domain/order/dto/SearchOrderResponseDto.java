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
}
