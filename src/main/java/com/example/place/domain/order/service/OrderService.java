package com.example.place.domain.order.service;

import org.springframework.stereotype.Service;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.service.ItemService;
import com.example.place.domain.order.dto.CreateOrderRequestDto;
import com.example.place.domain.order.dto.CreateOrderResponseDto;
import com.example.place.domain.order.entity.Order;
import com.example.place.domain.order.entity.OrderStatus;
import com.example.place.domain.order.repository.OrderRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
// @RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final UserService userService;
	private final ItemService itemService;


	public OrderService(OrderRepository orderRepository,UserService userService, ItemService itemService) {
		this.orderRepository = orderRepository;
		this.userService = userService;
		this.itemService = itemService;
	}

	@Transactional
	public CreateOrderResponseDto createOrder(CreateOrderRequestDto requestDto,Long userId
	) {
		User user = userService.findUserById(userId);

		Item item = itemService.findItemById(requestDto.getItemId());

		// 수량 유무 확인
		if (item.getCount() < requestDto.getQuantity()) {
			throw new CustomException(ExceptionCode.OUT_OF_STOCK);
		}
		Order order = Order.builder()
			.user(user)
			.item(item)
			.quantity(requestDto.getQuantity())
			.price(item.getPrice() * requestDto.getQuantity())
			.status(OrderStatus.PENDING)
			.deliveryAddress(requestDto.getDeliveryAddress())
			.completeAt(null)
			.build();

		orderRepository.save(order);

		// 주문으로 인한 재고 차감
		item.decreaseStock(requestDto.getQuantity());

		return CreateOrderResponseDto.from(order);
	}
}
