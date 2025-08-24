package com.example.place.domain.item.service;


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
    private final Long userId = 1L;
    private final Long itemId = 1L;

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
        // 이 테스트에만 필요한 spy 객체 생성
        Item spyItem = Mockito.spy(testItem);
        User spyUser = Mockito.spy(testUser);

        // spy 객체에 대한 행동 정의
        doReturn(1L).when(spyItem).getId();
        doReturn(spyUser).when(spyItem).getUser();
        doReturn(1L).when(spyUser).getId();


        // itemRepository의 findById 메서드가 호출될 때 spyItem 반환하도록 설정
        given(itemRepository.findById(anyLong())).willReturn(Optional.of(spyItem));
        given(imageService.updateImages(any(Item.class), anyList(), anyInt()))
                .willReturn(updateImageDto);

        //when
        ItemResponse response = itemService.updateItem(spyItem.getId(), updateRequest, spyUser.getId());
        //then
        assertThat(response.getItemName()).isEqualTo("수정된 아이템 이름");
        assertThat(response.getMainIndex()).isEqualTo(1);
        assertThat(response.getImageUrls()).isEqualTo(List.of("updateUrl1, updateUrl2"));
    }
    @Test
    @DisplayName("소프트 딜리트 성공")
    void softDelete() {
        //given
        Item spyItem = Mockito.spy(testItem);
        User spyUser = Mockito.spy(testUser);

        doReturn(userId).when(spyUser).getId();
        doReturn(spyUser).when(spyItem).getUser();
        given(itemRepository.findById(anyLong())).willReturn(Optional.of(spyItem));

        //when
        itemService.softDeleteItem(itemId, userId);

        //then
        verify(spyItem, times(1)).delete();

        verify(itemRepository, never()).deleteById(anyLong());
    }
}
