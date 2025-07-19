package com.example.place.domain.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateOrderRequestDto {
	@NotNull(message = "아이템 ID는 필수입니다.")
	private Long itemId;

	@Min(value = 1, message = "상품 개수는 1개 이상이어야 합니다.")
	private int quantity;

	@NotBlank(message = "배송지 주소는 필수입니다.")
	private String deliveryAddress;
}
