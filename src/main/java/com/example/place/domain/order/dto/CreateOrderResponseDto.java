package com.example.place.domain.order.dto;

import java.time.LocalDateTime;

import com.example.place.domain.order.entity.Order;

import lombok.Getter;

@Getter
public class CreateOrderResponseDto {

	private String mainImageUrl;
	private String userNickname;
	private String itemName;
	private Long quantity;
	private double price;
	private String status;
	private String deliveryAddress;
	private LocalDateTime createdAt;

	public CreateOrderResponseDto(String mainImageUrl, String userNickname, String itemName, Long quantity, double price,
		String status, String deliveryAddress, LocalDateTime createdAt) {
		this.mainImageUrl = mainImageUrl;
		this.userNickname = userNickname;
		this.itemName = itemName;
		this.quantity = quantity;
		this.price = price;
		this.status = status;
		this.deliveryAddress = deliveryAddress;
		this.createdAt = createdAt;
	}

	public static CreateOrderResponseDto from(Order order, String mainImageUrl) {
		return new CreateOrderResponseDto(
			mainImageUrl,
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
