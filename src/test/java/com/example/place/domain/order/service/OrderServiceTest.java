package com.example.place.domain.order.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.place.common.dto.PageResponseDto;
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

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

	private static final User TEST_USER = User.of(
		"testName",
		"testNickname",
		"test@email.com",
		"Test1234!",
		"url",
		UserRole.USER
	);

	private static final Item TEST_ITEM = Item.of(
		TEST_USER,
		"testItem",
		"testDescription",
		20000.0,
		2L,
		true,
		LocalDateTime.parse("2025-07-01T00:00:00"),
		LocalDateTime.parse("2026-07-01T00:00:00")
	);

	// id 강제 설정
	static {
		ReflectionTestUtils.setField(TEST_USER, "id", 1L);
		ReflectionTestUtils.setField(TEST_ITEM, "id", 2L);
	}

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private UserService userService;

	@Mock
	private ItemService itemService;

	@Mock
	private ImageService imageService;

	@Mock
	private StockService stockService;

	@InjectMocks
	private OrderService orderService;

	@Test
	void 상품_등록_성공() {

		//given
		Long userId = 1L;
		Long itemId = 2L;
		Long quantity = 2L;

		CreateOrderRequestDto requestDto = new CreateOrderRequestDto(itemId, quantity, "부산");

		// 유저 아이템 조회
		given(userService.findByIdOrElseThrow(userId)).willReturn(TEST_USER);
		given(itemService.findByIdOrElseThrow(itemId)).willReturn(TEST_ITEM);
		given(imageService.getMainImageUrl(itemId)).willReturn("image1url");

		willDoNothing().given(stockService).decreaseStock(itemId, quantity);

		// 저장되는지 검증
		given(orderRepository.save(any(Order.class)))
			.willAnswer(invocation -> invocation.getArgument(0));

		// when
		CreateOrderResponseDto responseDto = orderService.createOrder(requestDto, userId);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.getItemName()).isEqualTo(TEST_ITEM.getItemName());
		assertThat(responseDto.getDeliveryAddress()).isEqualTo("부산");
		assertThat(responseDto.getMainImageUrl()).isEqualTo("image1url");
	}


	@Test
	void 내_주문_단건_조회_성공() {
		// given
		Long orderId = 1L;
		Long userId = 1L;

		Order order = new Order(TEST_USER, TEST_ITEM, 2L, 40000.0, OrderStatus.ORDERED, "부산", null);
		ReflectionTestUtils.setField(order, "id", orderId);

		given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
		given(itemService.findByIdOrElseThrow(TEST_ITEM.getId())).willReturn(TEST_ITEM);
		given(imageService.getMainImageUrl(TEST_ITEM.getId())).willReturn("image1url");

		// when
		SearchOrderResponseDto responseDto = orderService.getMyOrder(orderId, userId);

		// then
		assertThat(responseDto).isNotNull();
	}

	@Test
	void 내_주문_전체_조회_성공() {
		// given
		Long userId = 1L;
		Pageable pageable = PageRequest.of(0, 10);

		Order order1 = new Order(TEST_USER, TEST_ITEM, 2L, 40000.0, OrderStatus.ORDERED, "부산", null);
		Order order2 = new Order(TEST_USER, TEST_ITEM, 1L, 20000.0, OrderStatus.READY, "서울", null);
		List<Order> orderList;
		orderList = Arrays.asList(order1, order2);
		Page<Order> orderPage = new PageImpl<>(orderList, pageable, orderList.size());

		given(orderRepository.findAllByUserIdWithItemAndUser(userId, pageable)).willReturn(orderPage);
		given(imageService.getMainImageUrl(TEST_ITEM.getId())).willReturn("image1url");

		// when
		PageResponseDto<SearchOrderResponseDto> responseDto = orderService.getAllMyOrders(userId, pageable);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.getContent()).hasSize(2);
	}

	@Test
	void 주문상태_준비로_변경_성공() {
		// given
		Long orderId = 1L;
		Long userId = 1L;

		Order order = new Order(TEST_USER, TEST_ITEM, 2L, 40000.0, OrderStatus.ORDERED, "부산", null);
		ReflectionTestUtils.setField(order, "id", orderId);

		given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
		given(imageService.getMainImageUrl(TEST_ITEM.getId())).willReturn("image1url");

		// when
		UpdateOrderStatusResponseDto responseDto = orderService.updateOrderStatusToReady(orderId, userId);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.getStatus()).isEqualTo("READY");
		assertThat(responseDto.getMainImageUrl()).isEqualTo("image1url");
	}

	@Test
	void 주문상태_거래완료_변경_성공() {
		// given
		Long orderId = 1L;
		Long userId = 1L;

		Order order = new Order(TEST_USER, TEST_ITEM, 2L, 40000.0, OrderStatus.READY, "부산", null);
		ReflectionTestUtils.setField(order, "id", orderId);

		given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
		given(imageService.getMainImageUrl(TEST_ITEM.getId())).willReturn("image1url");

		// when
		UpdateOrderStatusResponseDto responseDto = orderService.updateOrderStatusToCompleted(orderId, userId);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.getStatus()).isEqualTo("COMPLETED");
		assertThat(responseDto.getMainImageUrl()).isEqualTo("image1url");
	}

	@Test
	void 주문상태_거래취소_변경_성공() {
		// given
		Long orderId = 1L;
		Long userId = 1L;

		Order order = new Order(TEST_USER, TEST_ITEM, 2L, 40000.0, OrderStatus.ORDERED, "부산", null);
		ReflectionTestUtils.setField(order, "id", orderId);

		given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
		given(imageService.getMainImageUrl(TEST_ITEM.getId())).willReturn("image1url");

		// when
		UpdateOrderStatusResponseDto responseDto = orderService.updateOrderStatusToCanceled(orderId, userId);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.getStatus()).isEqualTo("CANCELLED");
		assertThat(responseDto.getMainImageUrl()).isEqualTo("image1url");
	}


}
