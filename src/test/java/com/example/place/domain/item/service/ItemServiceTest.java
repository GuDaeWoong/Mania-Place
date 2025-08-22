package com.example.place.domain.item.service;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.Image.dto.ImageDto;
import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.item.dto.request.ItemRequest;
import com.example.place.domain.item.dto.response.ItemResponse;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.repository.ItemRepository;
import com.example.place.domain.tag.service.TagService;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.repository.UserRepository;
import com.example.place.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private ImageService imageService;
    @Mock
    private TagService tagService;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ItemService itemService;


    private User testUser;
    private ItemRequest itemRequest;
    private ImageDto imageDto;
    private Item testItem;
    private Long userId = 1L;
    private Long itemId = 1L;

    @BeforeEach
    void setUp() {
        // 각 테스트 메서드 실행 전에 객체들을 초기화
        testUser = User.of(
                "김테스트",
                "김테",
                "123@gmail.com",
                "123",
                "image",
                UserRole.USER
        );
        testUser = Mockito.spy(testUser);
        doReturn(1L).when(testUser).getId();

        itemRequest = ItemRequest.builder()
                .itemName("테스트 상품")
                .itemDescription("상품설명")
                .price(1000.0)
                .count(3L)
                .isLimited(true)
                .imageUrls(List.of("image1", "image2"))
                .mainIndex(0)
                .build();

        imageDto = ImageDto.of(List.of("image1", "image2"), 0);

        testItem = Item.of(
                testUser,
                "테스트 상품",
                "설명",
                1000.0,
                10L,
                false,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10)
        );
        testItem = Mockito.spy(testItem);
        doReturn(1L).when(testItem).getId();
    }

    @Test
    @DisplayName("아이템 생성 테스트 : 성공")
    void createItem() {
        //given
        given(userService.findByIdOrElseThrow(any())).willReturn(testUser);
        given(itemRepository.save(any(Item.class))).willReturn(testItem);
        given(imageService.createImages(any(Item.class), anyList(), anyInt())).willReturn(imageDto);

        //when
        ItemResponse response = itemService.createItem(userId, itemRequest);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getItemName()).isEqualTo("테스트 상품");
        assertThat(response.getMainIndex()).isEqualTo(0);
    }

    @Test
    @DisplayName("아이템 조회 테스트 : 성공")
    void getItem() {
        // given
        given(itemRepository.findById(itemId)).willReturn(Optional.of(testItem));
        given(imageService.getItemImages(itemId)).willReturn(imageDto);

        // when
        ItemResponse response = itemService.getItem(itemId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getItemName()).isEqualTo(testItem.getItemName());
        assertThat(response.getImageUrls()).isEqualTo(imageDto.getImageUrls());
        assertThat(response.getMainIndex()).isEqualTo(imageDto.getMainIndex());
    }

    @Test
    @DisplayName("아이템 수정 테스트 : 성공")
    void updateItem() {
        //given
        ImageDto updateImageDto = ImageDto.of(List.of("updateUrl1, updateUrl2"), 1);
        ItemRequest updateRequest = ItemRequest.builder()
                .itemName("수정된 아이템 이름")
                .imageUrls(List.of("xxx", "ccc"))
                .mainIndex(1).build();
        given(itemRepository.findById(anyLong())).willReturn(Optional.of(testItem));
        given(imageService.updateImages(any(Item.class), anyList(), anyInt()))
                .willReturn(updateImageDto);
        //when
        ItemResponse response = itemService.updateItem(itemId, updateRequest, userId);
        //then
        assertThat(response.getItemName()).isEqualTo("수정된 아이템 이름");
        assertThat(response.getMainIndex()).isEqualTo(1);
        assertThat(response.getImageUrls()).isEqualTo(List.of("updateUrl1, updateUrl2"));
    }

    @Test
    @DisplayName("아이템 softDelte")
    void softDeleteItme() {
        //given
        given(itemRepository.findById(anyLong())).willReturn(Optional.of(testItem));

        //when
        itemService.softDeleteItem(itemId, userId);

        //then
        verify(testItem, times(1)).delete();
        verify(itemRepository, times(2)).findById(anyLong());
    }
//    @Test
//    @DisplayName("아이템 논리적 삭제 테스트 : 권한 없음")
//    void softDeleteItemForbidden() {
//        // given
//        // 다른 사용자의 ID를 준비
//        Long anotherUserId = 2L;
//
//        // findByIdOrElseThrow가 호출되면 testItem을 반환하도록 설정
//        given(itemRepository.findById(anyLong())).willReturn(Optional.of(testItem));
//
//        // when & then
//        // CustomException이 발생하는지 검증
//        assertThatThrownBy(() -> itemService.softDeleteItem(itemId, anotherUserId))
//                .isInstanceOf(CustomException.class)
//                .hasMessage(ExceptionCode.FORBIDDEN_ITEM_DELETE.getMessage());
//
//        // delete() 메서드가 호출되지 않았는지 검증
//        verify(testItem, times(0)).delete();
//    }

}
