package com.example.place.domain.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

		Item item = itemService.findByIdOrElseThrow(requestDto.getItemId());

		// 수량 유무 확인
		if (item.getCount() < requestDto.getQuantity()) {
			throw new CustomException(ExceptionCode.OUT_OF_STOCK);
		}
		Order order = new Order(user,
			item,
			requestDto.getQuantity(),
			item.getPrice()*requestDto.getQuantity(),
			OrderStatus.PENDING,
			requestDto.getDeliveryAddress(),
			null
		);

		orderRepository.save(order);

		// 주문으로 인한 재고 차감
		itemService.decreaseStock(item.getId(),requestDto.getQuantity());

		return CreateOrderResponseDto.from(order);
	}
}
