// package com.example.place.domain.order.service;
//
// import static org.assertj.core.api.AssertionsForClassTypes.*;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.BDDMockito.*;
//
// import java.time.LocalDateTime;
//
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.test.util.ReflectionTestUtils;
//
// import com.example.place.common.exception.exceptionclass.CustomException;
// import com.example.place.domain.item.entity.Item;
// import com.example.place.domain.item.service.ItemService;
// import com.example.place.domain.order.dto.request.CreateOrderRequestDto;
// import com.example.place.domain.order.dto.response.CreateOrderResponseDto;
// import com.example.place.domain.order.entity.Order;
// import com.example.place.domain.order.repository.OrderRepository;
// import com.example.place.domain.user.entity.User;
// import com.example.place.domain.user.entity.UserRole;
// import com.example.place.domain.user.service.UserService;
//
// @ExtendWith(MockitoExtension.class)
// public class OrderServiceTest {
//
// 	private static final User TEST_USER = User.of(
// 		"testName",
// 		"testNickname",
// 		"test@email.com",
// 		"Test1234!",
// 		"url",
// 		UserRole.USER
// 	);
//
// 	private static final Item TEST_ITEM = Item.of(
// 		TEST_USER,
// 		"testItem",
// 		"testDescription",
// 		20000.0,
// 		2L,
// 		LocalDateTime.parse("2025-07-01T00:00:00"),
// 		LocalDateTime.parse("2026-07-01T00:00:00")
// 	);
//
// 	// id 강제 설정
// 	static {
// 		ReflectionTestUtils.setField(TEST_ITEM, "id", 2L);
// 	}
//
// 	@Mock
// 	private OrderRepository orderRepository;
//
// 	@Mock
// 	private UserService userService;
//
// 	@Mock
// 	private ItemService itemService;
//
// 	@InjectMocks
// 	private OrderService orderService;
//
// 	@Test
// 	void 상품_등록_성공() {
//
// 		//given
// 		Long userId = 1L;
// 		Long itemId = 2L;
// 		Long quantity = 2L;
//
// 		CreateOrderRequestDto requestDto = new CreateOrderRequestDto(itemId, quantity, "부산");
//
// 		// 유저 아이템 조회
// 		given(userService.findByIdOrElseThrow(userId)).willReturn(TEST_USER);
// 		given(itemService.findByIdOrElseThrow(itemId)).willReturn(TEST_ITEM);
// 		given(itemService.getMainImageUrl(itemId)).willReturn("image1url");
//
// 		// 저장되는지 검증
// 		given(orderRepository.save(any(Order.class)))
// 			.willAnswer(invocation -> invocation.getArgument(0));
//
// 		// when
// 		CreateOrderResponseDto responseDto = orderService.createOrder(requestDto, userId);
//
// 		// then
// 		assertThat(responseDto).isNotNull();
// 		assertThat(responseDto.getItemName()).isEqualTo(TEST_ITEM.getItemName());
// 		assertThat(responseDto.getDeliveryAddress()).isEqualTo("부산");
// 		assertThat(responseDto.getMainImageUrl()).isEqualTo("image1url");
// 	}
//
//
// 	@Test
// 	void 상품_재고부족_주문_실패() {
// 		//given
// 		Long userId = 1L;
// 		Long itemId = 2L;
// 		Long quantity = 3L;
// 		CreateOrderRequestDto requestDto = new CreateOrderRequestDto(itemId, quantity, "부산");
//
// 		given(userService.findByIdOrElseThrow(userId)).willReturn(TEST_USER);
// 		given(itemService.findByIdOrElseThrow(itemId)).willReturn(TEST_ITEM);
//
// 		CustomException exception = assertThrows(CustomException.class, () ->
// 			orderService.createOrder(requestDto, userId)
// 		);
// 		assertThat(exception.getExceptionCode().name()).isEqualTo("OUT_OF_STOCK");
// 	}
//
// }
