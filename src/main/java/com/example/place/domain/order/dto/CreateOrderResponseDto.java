package com.example.place.domain.order.dto;

import java.time.LocalDateTime;

import com.example.place.domain.order.entity.Order;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateOrderResponseDto {

	private String userNickname;
	private String itemName;
	private int quantity;
	private double price;
	private String status;
	private String deliveryAddress;
	private LocalDateTime createdAt;

	public CreateOrderResponseDto(String userNickname, String itemName, int quantity, double price,
		String status, String deliveryAddress, LocalDateTime createdAt) {
		this.userNickname = userNickname;
		this.itemName = itemName;
		this.quantity = quantity;
		this.price = price;
		this.status = status;
		this.deliveryAddress = deliveryAddress;
		this.createdAt = createdAt;
	}

	public static CreateOrderResponseDto from(Order order) {
		return new CreateOrderResponseDto(
			order.getUser().getNickname(),
			order.getItem().getItemName(),
			order.getQuantity(),
			order.getPrice(),
			order.getStatus().name(),
			order.getDeliveryAddress(),
			order.getCreatedAt()
		);
	}

}
