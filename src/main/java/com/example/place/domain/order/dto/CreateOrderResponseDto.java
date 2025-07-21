package com.example.place.domain.order.dto;

import java.time.LocalDateTime;

import com.example.place.domain.order.entity.Order;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateOrderResponseDto {

	private String userNickname;
	private String itemName;
	private int quantity;
	private double price;
	private String status;
	private String deliveryAddress;
	private LocalDateTime createdAt;

	public static CreateOrderResponseDto from(Order order) {
		return CreateOrderResponseDto.builder()
			.userNickname(order.getUser().getNickname())
			.itemName(order.getItem().getItemName())
			.quantity(order.getQuantity())
			.price(order.getPrice())
			.status(order.getStatus().name()) // enum to string
			.deliveryAddress(order.getDeliveryAddress())
			.createdAt(order.getCreatedAt())
			.build();
	}
}
