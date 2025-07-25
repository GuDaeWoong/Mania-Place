package com.example.place.domain.order.dto.response;

import java.time.LocalDateTime;

import com.example.place.domain.order.entity.Order;

import lombok.Getter;

@Getter
public class UpdateOrderStatusResponseDto {
	private String mainImageUrl;
	private String userNickname;
	private String itemName;
	private Long quantity;
	private double price;
	private String status;
	private String deliveryAddress;
	private LocalDateTime createdAt;

	private UpdateOrderStatusResponseDto(String mainImageUrl, String nickname, String itemName, Long quantity,
		double price, String deliveryAddress, String status, LocalDateTime createdAt) {
		this.mainImageUrl = mainImageUrl;
		this.userNickname = nickname;
		this.itemName = itemName;
		this.quantity = quantity;
		this.price = price;
		this.status = status;
		this.deliveryAddress = deliveryAddress;
		this.createdAt = createdAt;
	}

	public static UpdateOrderStatusResponseDto from(Order order, String mainImageUrl){
		return new UpdateOrderStatusResponseDto(
			mainImageUrl,
			order.getUser().getNickname(),
			order.getItem().getItemName(),
			order.getQuantity(),
			order.getPrice(),
			order.getDeliveryAddress(),
			order.getStatus().name(),
			order.getCreatedAt()
		);
	}


}
