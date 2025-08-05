package com.example.place.domain.order.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.annotation.Loggable;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.service.ItemService;
import com.example.place.domain.item.service.StockService;
import com.example.place.domain.order.dto.request.CreateOrderRequestDto;
import com.example.place.domain.order.dto.response.CreateOrderResponseDto;
import com.example.place.domain.order.dto.response.SearchOrderResponseDto;
import com.example.place.domain.order.dto.response.UpdateOrderStatusResponseDto;
import com.example.place.domain.order.entity.Order;
import com.example.place.domain.order.entity.OrderStatus;
import com.example.place.domain.order.repository.OrderRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final UserService userService;
	private final ItemService itemService;
	private final StockService stockService;
	private final ImageService imageService;

	@Loggable
	@Transactional
	public CreateOrderResponseDto createOrder(CreateOrderRequestDto requestDto,Long userId
	) {
		User user = userService.findByIdOrElseThrow(userId);

		Item item = itemService.findByIdOrElseThrow(requestDto.getItemId());

		// 상품 소프트딜리트 확인
		if (item.isDeleted()) {
			throw new CustomException(ExceptionCode.NOT_FOUND_ITEM);
		}

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
		stockService.decreaseStock(item.getId(),requestDto.getQuantity());

		// 메인 이미지 추가
		String mainImageUrl = imageService.getMainImageUrl(item.getId());

		return CreateOrderResponseDto.from(order,mainImageUrl);
	}

	@Loggable
	public SearchOrderResponseDto getMyOrder(Long orderId, Long userId) {

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ORDER));

		if (!order.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_ORDER_ACCESS);
		}

		Item item = itemService.findByIdOrElseThrow(order.getItem().getId());
		String mainImageUrl = imageService.getMainImageUrl(item.getId());

		return SearchOrderResponseDto.from(order,mainImageUrl);
	}

	@Loggable
	public PageResponseDto<SearchOrderResponseDto> getAllMyOrders(Long userId, Pageable pageable
	) {
		Page<Order> orders = orderRepository.findAllByUserIdWithItemAndUser(userId, pageable);

		Page<SearchOrderResponseDto> dtoPage = orders.map(order -> {
			Item item = order.getItem();
			String mainImageUrl = imageService.getMainImageUrl(item.getId());

			return SearchOrderResponseDto.from(order,mainImageUrl
			);
		});
		return new PageResponseDto<>(dtoPage);
	}


	@Loggable
	@Transactional
	public UpdateOrderStatusResponseDto updateOrderStatusToReady(Long orderId, Long userId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ORDER));

		// 상품 등록한 유저와 동일한지 검증
		Item item = order.getItem();
		if (!item.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.UNAUTHORIZED_STATUS_CHANGE);
		}
		OrderStatus.statusIsReady(order.getStatus());

		order.updateStatus(OrderStatus.READY);

		String mainImageUrl = imageService.getMainImageUrl(item.getId());

		return UpdateOrderStatusResponseDto.from(order,mainImageUrl);
	}

	@Loggable
	@Transactional
	public UpdateOrderStatusResponseDto updateOrderStatusToCompleted(Long orderId, Long userId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ORDER));

		// 주문자 인지 아닌지 검증
		if (!order.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.NOT_SELLER);
		}

		OrderStatus.statusIsCompleted(order.getStatus());

		order.updateStatus(OrderStatus.COMPLETED);

		Item item = order.getItem();
		String mainImageUrl = imageService.getMainImageUrl(item.getId());

		return UpdateOrderStatusResponseDto.from(order,mainImageUrl);
	}

	@Loggable
	@Transactional
	public UpdateOrderStatusResponseDto updateOrderStatusToCanceled(Long orderId, Long userId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ORDER));

		if (!order.getUser().getId().equals(userId) && !order.getUser().getRole().equals(UserRole.ADMIN)) {
			throw new CustomException(ExceptionCode.ORDER_CANCEL_ACCESS_DENIED);
		}

		// 메서드명 추후 변경예정
		OrderStatus.statusIsReady(order.getStatus());

		order.updateStatus(OrderStatus.CANCELLED);

		Item item = order.getItem();

		// 주문취소 인한 재고 증가
		stockService.increaseStock(item.getId(),order.getQuantity());

		String mainImageUrl = imageService.getMainImageUrl(item.getId());

		return UpdateOrderStatusResponseDto.from(order,mainImageUrl);
	}

	@Transactional
	public void removeItemReferenceFromOrders(Long itemId) {
		// 삭제 전 해당 상품의 진행 중인 주문이 없는 확인
		boolean hasActiveOrders = orderRepository.existsByItemIdAndStatusIn(itemId,
			List.of(OrderStatus.ORDERED, OrderStatus.READY));
		if (hasActiveOrders) {
			throw new CustomException(ExceptionCode.ITEM_HAS_ACTIVE_ORDERS);
		}

		// 주문 테이블의 상품 외래키 삭제
		List<Order> orders = orderRepository.findByItemId(itemId);
		for (Order order : orders) {
			order.clearItem();
		}
	}

}
