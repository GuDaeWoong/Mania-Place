package com.example.place.domain.item.service;


import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.Image.dto.ImageDto;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.item.dto.ItemsAndIsFindByUserTag;
import com.example.place.domain.item.dto.request.ItemRequest;
import com.example.place.domain.item.dto.response.ItemGetAllResponse;
import com.example.place.domain.item.dto.response.ItemResponse;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.repository.ItemRepository;
import com.example.place.domain.keyword.service.SearchKeywordService;
import com.example.place.domain.tag.service.TagService;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.within;
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
    private SearchKeywordService searchKeywordService;
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
                .itemTagNames(Set.of("tag1", "tag2"))
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
        verify(tagService).saveTags(any(Item.class), any(Set.class));
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
    @DisplayName("아이템 조회 테스트 : 존재하지 않는 아이템")
    void getItem_NotFound() {
        // given
        given(itemRepository.findById(itemId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> itemService.getItem(itemId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.NOT_FOUND_ITEM.getMessage());
    }

    @Test
    @DisplayName("아이템 조회 테스트 : 삭제된 아이템")
    void getItem_Deleted() {
        // given
        Item deletedItem = spy(testItem);
        doReturn(true).when(deletedItem).isDeleted();
        given(itemRepository.findById(itemId)).willReturn(Optional.of(deletedItem));

        // when, then
        assertThatThrownBy(() -> itemService.getItem(itemId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.NOT_FOUND_ITEM.getMessage());
    }

    @Test
    @DisplayName("아이템 검색 테스트 : 성공")
    void searchItems() {
        // given
        String keyword = "테스트";
        List<String> tags = List.of("tag1", "tag2");
        Pageable pageable = PageRequest.of(0, 10);

        Item spyItem = spy(testItem);
        doReturn(itemId).when(spyItem).getId();

        Page<Item> pagedItems = new PageImpl<>(List.of(spyItem));
        Image mainImage = mock(Image.class);

        given(itemRepository.search(keyword, tags, userId, pageable)).willReturn(pagedItems);
        given(imageService.getMainImagesForItems(pagedItems)).willReturn(Map.of(itemId, mainImage));
        given(mainImage.getImageUrl()).willReturn("main_image_url");

        // when
        PageResponseDto<ItemGetAllResponse> response = itemService.searchItems(keyword, tags, userId, pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        verify(searchKeywordService).addKeyword(keyword);
    }

    @Test
    @DisplayName("사용자 태그 기반 전체 아이템 조회 테스트 : 태그 기반 결과 존재")
    void getAllItemsWithUserTag_WithTagResults() {
        // given
        CustomPrincipal principal = mock(CustomPrincipal.class);
        given(principal.getId()).willReturn(userId);
        given(userService.findByIdOrElseThrow(userId)).willReturn(testUser);

        Item spyItem = spy(testItem);
        doReturn(itemId).when(spyItem).getId();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> pagedItems = new PageImpl<>(List.of(spyItem));
        Image mainImage = mock(Image.class);

        given(itemRepository.findByUserTag(testUser, pageable)).willReturn(pagedItems);
        given(imageService.getMainImagesForItems(pagedItems)).willReturn(Map.of(itemId, mainImage));
        given(mainImage.getImageUrl()).willReturn("main_image_url");

        // when
        ItemsAndIsFindByUserTag response = itemService.getAllItemsWIthUserTag(principal, pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.isFindByUserTag()).isTrue();
        assertThat(response.getResponse().getContent()).hasSize(1);
    }

    @Test
    @DisplayName("사용자 태그 기반 전체 아이템 조회 테스트 : 태그 기반 결과 없을시 전체 조회")
    void getAllItemsWithUserTag_FallbackToAll() {
        // given
        CustomPrincipal principal = mock(CustomPrincipal.class);
        given(principal.getId()).willReturn(userId);
        given(userService.findByIdOrElseThrow(userId)).willReturn(testUser);

        Item spyItem = spy(testItem);
        doReturn(itemId).when(spyItem).getId();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> emptyTagPage = new PageImpl<>(Collections.emptyList());
        Page<Item> allItemsPage = new PageImpl<>(List.of(spyItem));
        Image mainImage = mock(Image.class);

        given(itemRepository.findByUserTag(testUser, pageable)).willReturn(emptyTagPage);
        given(itemRepository.findAllCustom(pageable)).willReturn(allItemsPage);
        given(imageService.getMainImagesForItems(allItemsPage)).willReturn(Map.of(itemId, mainImage));
        given(mainImage.getImageUrl()).willReturn("main_image_url");

        // when
        ItemsAndIsFindByUserTag response = itemService.getAllItemsWIthUserTag(principal, pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.isFindByUserTag()).isFalse();
        assertThat(response.getResponse().getContent()).hasSize(1);
    }

    @Test
    @DisplayName("아이템 수정 테스트 : 성공")
    void updateItem() {
        //given
        ImageDto updateImageDto = ImageDto.of(List.of("updateUrl1", "updateUrl2"), 1);
        ItemRequest updateRequest = ItemRequest.builder()
                .itemName("수정된 아이템 이름")
                .imageUrls(List.of("xxx", "ccc"))
                .mainIndex(1)
                .itemTagNames(Set.of("newTag1", "newTag2"))
                .build();

        Item spyItem = spy(testItem);
        User spyUser = spy(testUser);
        doReturn(itemId).when(spyItem).getId();
        doReturn(spyUser).when(spyItem).getUser();
        doReturn(userId).when(spyUser).getId();

        given(itemRepository.findById(itemId)).willReturn(Optional.of(spyItem));
        given(imageService.updateImages(any(Item.class), anyList(), anyInt())).willReturn(updateImageDto);

        //when
        ItemResponse response = itemService.updateItem(itemId, updateRequest, userId);

        //then
        assertThat(response.getItemName()).isEqualTo("수정된 아이템 이름");
        assertThat(response.getMainIndex()).isEqualTo(1);
        verify(spyItem).updateItem(updateRequest);
        verify(tagService).saveTags(spyItem, updateRequest.getItemTagNames());
    }

    @Test
    @DisplayName("아이템 수정 테스트 : 권한 없음")
    void updateItem_Forbidden() {
        //given
        Long otherUserId = 3L;
        ItemRequest updateRequest = ItemRequest.builder()
                .itemName("수정된 아이템 이름")
                .build();

        Item spyItem = spy(testItem);
        User spyUser = spy(testUser);
        doReturn(spyUser).when(spyItem).getUser();
        doReturn(userId).when(spyUser).getId();

        given(itemRepository.findById(itemId)).willReturn(Optional.of(spyItem));

        //when, then
        assertThatThrownBy(() -> itemService.updateItem(itemId, updateRequest, otherUserId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.FORBIDDEN_ITEM_ACCESS.getMessage());
    }

    @Test
    @DisplayName("아이템 수정 테스트 : 이미지 URL만 있고 메인 인덱스 없음")
    void updateItem_InvalidImageRequest_UrlsWithoutIndex() {
        //given
        ItemRequest updateRequest = ItemRequest.builder()
                .itemName("수정된 아이템 이름")
                .imageUrls(List.of("xxx", "ccc"))
                .build(); // mainIndex가 null

        Item spyItem = spy(testItem);
        User spyUser = spy(testUser);
        doReturn(spyUser).when(spyItem).getUser();
        doReturn(userId).when(spyUser).getId();

        given(itemRepository.findById(itemId)).willReturn(Optional.of(spyItem));

        //when, then
        assertThatThrownBy(() -> itemService.updateItem(itemId, updateRequest, userId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.INVALID_IMAGE_UPDATE_REQUEST.getMessage());
    }

    @Test
    @DisplayName("아이템 수정 테스트 : 메인 인덱스만 있고 이미지 URL 없음")
    void updateItem_InvalidImageRequest_IndexWithoutUrls() {
        //given
        ItemRequest updateRequest = ItemRequest.builder()
                .itemName("수정된 아이템 이름")
                .mainIndex(1)
                .build(); // imageUrls이 null

        Item spyItem = spy(testItem);
        User spyUser = spy(testUser);
        doReturn(spyUser).when(spyItem).getUser();
        doReturn(userId).when(spyUser).getId();

        given(itemRepository.findById(itemId)).willReturn(Optional.of(spyItem));

        //when, then
        assertThatThrownBy(() -> itemService.updateItem(itemId, updateRequest, userId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.INVALID_IMAGE_UPDATE_REQUEST.getMessage());
    }

    @Test
    @DisplayName("아이템 수정 테스트 : 이미지 수정 없이 기존 이미지 유지")
    void updateItem_KeepExistingImages() {
        //given
        ItemRequest updateRequest = ItemRequest.builder()
                .itemName("수정된 아이템 이름")
                .build(); // 이미지 관련 필드가 null

        Item spyItem = spy(testItem);
        User spyUser = spy(testUser);
        doReturn(itemId).when(spyItem).getId();
        doReturn(spyUser).when(spyItem).getUser();
        doReturn(userId).when(spyUser).getId();

        given(itemRepository.findById(itemId)).willReturn(Optional.of(spyItem));
        given(imageService.getItemImages(itemId)).willReturn(imageDto);

        //when
        ItemResponse response = itemService.updateItem(itemId, updateRequest, userId);

        //then
        verify(imageService, never()).updateImages(any(), any(), any());
        verify(imageService).getItemImages(itemId);
        assertThat(response.getImageUrls()).isEqualTo(imageDto.getImageUrls());
    }

    @Test
    @DisplayName("소프트 딜리트 성공")
    void softDelete() {
        //given
        Item spyItem = spy(testItem);
        User spyUser = spy(testUser);

        doReturn(userId).when(spyUser).getId();
        doReturn(spyUser).when(spyItem).getUser();
        given(itemRepository.findById(itemId)).willReturn(Optional.of(spyItem));

        //when
        itemService.softDeleteItem(itemId, userId);

        //then
        verify(spyItem, times(1)).delete();
        verify(itemRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("소프트 딜리트 실패 : 권한 없음")
    void softDelete_Forbidden() {
        //given
        Long otherUserId = 3L;
        Item spyItem = spy(testItem);
        User spyUser = spy(testUser);

        doReturn(userId).when(spyUser).getId();
        doReturn(spyUser).when(spyItem).getUser();
        given(itemRepository.findById(itemId)).willReturn(Optional.of(spyItem));

        //when, then
        assertThatThrownBy(() -> itemService.softDeleteItem(itemId, otherUserId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.FORBIDDEN_ITEM_DELETE.getMessage());

        verify(spyItem, never()).delete();
    }

    @Test
    @DisplayName("판매자 조회 테스트 : 성공")
    void getSeller() {
        //given
        given(itemRepository.findById(itemId)).willReturn(Optional.of(testItem));

        //when
        User seller = itemService.getSeller(itemId);

        //then
        assertThat(seller).isEqualTo(testUser);
    }

    @Test
    @DisplayName("findByIdOrElseThrow 테스트 : 성공")
    void findByIdOrElseThrow_Success() {
        //given
        given(itemRepository.findById(itemId)).willReturn(Optional.of(testItem));

        //when
        Item foundItem = itemService.findByIdOrElseThrow(itemId);

        //then
        assertThat(foundItem).isEqualTo(testItem);
    }

    @Test
    @DisplayName("findByIdOrElseThrow 테스트 : 아이템 없음")
    void findByIdOrElseThrow_NotFound() {
        //given
        given(itemRepository.findById(itemId)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> itemService.findByIdOrElseThrow(itemId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.NOT_FOUND_ITEM.getMessage());
    }

    @Test
    @DisplayName("findByIdOrElseThrow 테스트 : 삭제된 아이템")
    void findByIdOrElseThrow_DeletedItem() {
        //given
        Item deletedItem = spy(testItem);
        doReturn(true).when(deletedItem).isDeleted();
        given(itemRepository.findById(itemId)).willReturn(Optional.of(deletedItem));

        //when, then
        assertThatThrownBy(() -> itemService.findByIdOrElseThrow(itemId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.NOT_FOUND_ITEM.getMessage());
    }

    @Test
    @DisplayName("buildGetAllItems 테스트 : 성공")
    void buildGetAllItems() {
        //given
        Item spyItem = spy(testItem);
        doReturn(itemId).when(spyItem).getId();

        Page<Item> pagedItems = new PageImpl<>(List.of(spyItem));
        Image mainImage = mock(Image.class);
        given(imageService.getMainImagesForItems(pagedItems)).willReturn(Map.of(itemId, mainImage));
        given(mainImage.getImageUrl()).willReturn("main_image_url");

        //when
        PageResponseDto<ItemGetAllResponse> response = itemService.buildGetAllItems(pagedItems);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        verify(imageService).getMainImagesForItems(pagedItems);
    }

    @Test
    @DisplayName("아이템 생성 성공: salesStartAt이 null일 때 기본값 설정")
    void createItem_Success_WithNullSalesStartAt() {
        //given
        ItemRequest requestWithNullStart = ItemRequest.builder()
                .itemName("테스트 상품")
                .itemDescription("상품설명")
                .price(1000.0)
                .count(3L)
                .isLimited(true)
                .salesStartAt(null)
                .salesEndAt(LocalDateTime.of(2025, 1, 31, 23, 59))
                .imageUrls(List.of("image1", "image2"))
                .mainIndex(0)
                .itemTagNames(Set.of("tag1", "tag2"))
                .build();

        given(userService.findByIdOrElseThrow(any())).willReturn(testUser);
        given(itemRepository.save(any(Item.class))).willReturn(testItem);
        given(imageService.createImages(any(Item.class), anyList(), anyInt())).willReturn(imageDto);

        //when
        ItemResponse response = itemService.createItem(userId, requestWithNullStart);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getSalesStartAt()).isNotNull();
        assertThat(response.getSalesStartAt()).isBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now().plusSeconds(1));
        assertThat(response.getSalesEndAt()).isEqualTo(LocalDateTime.of(2025, 1, 31, 23, 59));
    }

    @Test
    @DisplayName("아이템 생성 성공: salesEndAt이 null일 때 기본값 설정")
    void createItem_Success_WithNullSalesEndAt() {
        //given
        ItemRequest requestWithNullEnd = ItemRequest.builder()
                .itemName("테스트 상품")
                .itemDescription("상품설명")
                .price(1000.0)
                .count(3L)
                .isLimited(true)
                .salesStartAt(LocalDateTime.of(2025, 1, 1, 0, 0))
                .salesEndAt(null)
                .imageUrls(List.of("image1", "image2"))
                .mainIndex(0)
                .itemTagNames(Set.of("tag1", "tag2"))
                .build();

        given(userService.findByIdOrElseThrow(any())).willReturn(testUser);
        given(itemRepository.save(any(Item.class))).willReturn(testItem);
        given(imageService.createImages(any(Item.class), anyList(), anyInt())).willReturn(imageDto);

        //when
        ItemResponse response = itemService.createItem(userId, requestWithNullEnd);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getSalesEndAt()).isEqualTo(LocalDateTime.of(3000, 1, 1, 0, 0));
    }

    @Test
    @DisplayName("아이템 생성: salesStartAt과 salesEndAt이 모두 null일 떄, 기본값으로 설정되는지 확인")
    void createItem_Success_WithBothNullSalesTime() {
        // given
        ItemRequest requestWithNulls = ItemRequest.builder()
                .itemName("테스트 상품")
                .itemDescription("상품설명")
                .price(1000.0)
                .count(3L)
                .imageUrls(List.of("image1", "image2"))
                .mainIndex(0)
                .itemTagNames(Set.of("tag1", "tag2"))
                .build();

        ItemService spyItemService = Mockito.spy(itemService);

        given(userService.findByIdOrElseThrow(any())).willReturn(testUser);
        given(itemRepository.save(any(Item.class))).willAnswer(invocation -> {
            Item item = invocation.getArgument(0);

            assertThat(item.getSalesStartAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
            assertThat(item.getSalesEndAt()).isEqualTo(LocalDateTime.of(3000, 1, 1, 0, 0));
            return item;
        });
        given(imageService.createImages(any(Item.class), anyList(), anyInt())).willReturn(imageDto);

        // when
        ItemResponse response = spyItemService.createItem(userId, requestWithNulls);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getSalesStartAt()).isNotNull();
        assertThat(response.getSalesEndAt()).isEqualTo(LocalDateTime.of(3000, 1, 1, 0, 0));
        verify(tagService).saveTags(any(Item.class), any(Set.class));
    }
}
