package com.example.place.domain.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.service.ItemService;
import com.example.place.domain.order.dto.CreateOrderRequestDto;
import com.example.place.domain.order.dto.CreateOrderResponseDto;
import com.example.place.domain.order.dto.SearchOrderResponseDto;
import com.example.place.domain.order.dto.UpdateOrderStatusResponseDto;
import com.example.place.domain.order.entity.Order;
import com.example.place.domain.order.entity.OrderStatus;
import com.example.place.domain.order.repository.OrderRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
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

		// 판매 기간 유무 검증
		item.validateSalesPeriod();

		Order order = new Order(user,
			item,
			requestDto.getQuantity(),
			item.getPrice()*requestDto.getQuantity(),
			OrderStatus.ORDERED,
			requestDto.getDeliveryAddress(),
			null
		);

		orderRepository.save(order);

		// 주문으로 인한 재고 차감
		itemService.decreaseStock(item.getId(),requestDto.getQuantity());

		// 메인 이미지 추가
		String mainImageUrl = itemService.getMainImageUrl(item.getId());

		return CreateOrderResponseDto.from(order,mainImageUrl);
	}

	public SearchOrderResponseDto getMyOrder(Long orderId, Long userId) {

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ORDER));

		if (!order.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_ORDER_ACCESS);
		}

		Item item = itemService.findByIdOrElseThrow(order.getItem().getId());
		String mainImageUrl = itemService.getMainImageUrl(item.getId());

		return new SearchOrderResponseDto(
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


	public Page<SearchOrderResponseDto> getAllMyOrders(Long userId, Pageable pageable) {

		Page<Order> orders = orderRepository.findByUserId(userId, pageable);

		List<SearchOrderResponseDto> responseDtoList = new ArrayList<>();

		for (Order order : orders) {
			Item item = itemService.findByIdOrElseThrow(order.getItem().getId());
			String mainImageUrl = itemService.getMainImageUrl(item.getId());

			SearchOrderResponseDto dto = new SearchOrderResponseDto(
				mainImageUrl,
				order.getUser().getNickname(),
				order.getItem().getItemName(),
				order.getQuantity(),
				order.getPrice(),
				order.getDeliveryAddress(),
				order.getStatus().name(),
				order.getCreatedAt()
			);
			responseDtoList.add(dto);
		}
		return new PageImpl<>(responseDtoList, pageable, orders.getTotalElements());
	}

	@Transactional
	public UpdateOrderStatusResponseDto updateOrderStatusToReady(Long orderId, Long userId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ORDER));

		// 상품 등록한 유저와 동일한지 검증
		Item item = order.getItem();
		if (!item.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_ITEM_ACCESS);
		}
		OrderStatus.statusToReady(order.getStatus());

		order.updateStatus(OrderStatus.READY);

		String mainImageUrl = itemService.getMainImageUrl(item.getId());

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

	public UpdateOrderStatusResponseDto updateOrderStatusToCompleted(Long orderId, Long userId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ORDER));

		// 주문자 인지 아닌지 검증
		if (!order.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_ORDER_ACCESS);
		}

		OrderStatus.statusToCompleted(order.getStatus());

		order.updateStatus(OrderStatus.COMPLETED);

		Item item = order.getItem();
		String mainImageUrl = itemService.getMainImageUrl(item.getId());

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

	@Transactional
	public UpdateOrderStatusResponseDto updateOrderStatusToCanceled(Long orderId, Long userId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ORDER));

		if (!order.getUser().getId().equals(userId) && !order.getUser().getRole().equals(UserRole.ADMIN)) {
			throw new CustomException(ExceptionCode.ORDER_CANCEL_ACCESS_DENIED);
		}

		// 메서드명 추후 변경예정
		OrderStatus.statusToReady(order.getStatus());

		order.updateStatus(OrderStatus.CANCELLED);

		Item item = order.getItem();

		// 주문취소 인한 재고 증가
		itemService.increaseStock(item.getId(),order.getQuantity());

		String mainImageUrl = itemService.getMainImageUrl(item.getId());

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
