package com.example.place.domain.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.domain.order.dto.CreateOrderRequestDto;
import com.example.place.domain.order.dto.CreateOrderResponseDto;
import com.example.place.domain.order.service.OrderService;

import jakarta.validation.Valid;

@RestController
// @RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<ApiResponseDto> createOrder(
		@Valid @RequestBody CreateOrderRequestDto requestDto
		// , @AuthenticationPrincipal CustomUserDetails userDetails
	){
		// Long userId =  userDetails.getId();
		Long userId = 1L;

		CreateOrderResponseDto createOrder = orderService.createOrder(requestDto,userId);
		ApiResponseDto<CreateOrderResponseDto> success = new ApiResponseDto<>("주문이 완료되었습니다", createOrder);
	return ResponseEntity.status(HttpStatus.OK).body(success);
	}
}
