package com.example.place.domain.order.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.order.dto.CreateOrderRequestDto;
import com.example.place.domain.order.dto.CreateOrderResponseDto;
import com.example.place.domain.order.dto.SearchOrderResponseDto;
import com.example.place.domain.order.dto.UpdateOrderStatusResponseDto;
import com.example.place.domain.order.service.OrderService;

import jakarta.validation.Valid;

@RestController
// @RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/orders")
	public ResponseEntity<ApiResponseDto> createOrder(
		@Valid @RequestBody CreateOrderRequestDto requestDto,
		@AuthenticationPrincipal CustomPrincipal userDetails
	){
		Long userId =  userDetails.getId();

		CreateOrderResponseDto createOrder = orderService.createOrder(requestDto,userId);
		ApiResponseDto<CreateOrderResponseDto> success = new ApiResponseDto<>("주문이 완료되었습니다", createOrder);
	return ResponseEntity.status(HttpStatus.OK).body(success);
	}

	@GetMapping("/orders/my/{orderId}")
	public ResponseEntity<ApiResponseDto> getMyOrder(
		@PathVariable Long orderId,
		@AuthenticationPrincipal CustomPrincipal userDetails
	){
		Long userId =  userDetails.getId();
		SearchOrderResponseDto searchOrder = orderService.getMyOrder(orderId, userId);
		ApiResponseDto<SearchOrderResponseDto> success = new ApiResponseDto<>("단건 조회가 완료되었습니다.", searchOrder);
		return ResponseEntity.ok(success);
	}


	@GetMapping("/orders/my")
	public ResponseEntity<ApiResponseDto> getAllMyOrders(
		@AuthenticationPrincipal CustomPrincipal userDetails,
		@PageableDefault Pageable pageable
	){
		Long userId = userDetails.getId();

		Page<SearchOrderResponseDto> orders = orderService.getAllMyOrders(userId, pageable);

		ApiResponseDto<Page<SearchOrderResponseDto>> success = new ApiResponseDto<>("전체 주문 조회가 완료되었습니다.", orders);
		return ResponseEntity.ok(success);
	}

	@PostMapping("/user/orders/{orderId}/status/ready")
	public ResponseEntity<ApiResponseDto> updateOrderStatusToReady(
		@PathVariable Long orderId,
		@AuthenticationPrincipal CustomPrincipal userDetails
	) {
		Long userId = userDetails.getId();
		UpdateOrderStatusResponseDto updatedOrder = orderService.updateOrderStatusToReady(orderId, userId);
		return ResponseEntity.ok(new ApiResponseDto<>("주문 상태 변경 완료", updatedOrder));
	}

	@PostMapping("/user/orders/{orderId}/status/completed")
	public ResponseEntity<ApiResponseDto> updateOrderStatusToCompleted(
		@PathVariable Long orderId,
		@AuthenticationPrincipal CustomPrincipal userDetails
	) {
		Long userId = userDetails.getId();
		UpdateOrderStatusResponseDto updatedOrder = orderService.updateOrderStatusToCompleted(orderId, userId);
		return ResponseEntity.ok(new ApiResponseDto<>("주문 상태 변경 완료", updatedOrder));
	}

	@PostMapping("/user/orders/{orderId}/status/canceled")
	public ResponseEntity<ApiResponseDto> cancelOrder(
		@PathVariable Long orderId,
		@AuthenticationPrincipal CustomPrincipal userDetails
	) {
		Long userId = userDetails.getId();
		UpdateOrderStatusResponseDto canceledOrder = orderService.updateOrderStatusToCanceled(orderId, userId);
		return ResponseEntity.ok(new ApiResponseDto<>("주문 취소 완료", canceledOrder));
	}

}
