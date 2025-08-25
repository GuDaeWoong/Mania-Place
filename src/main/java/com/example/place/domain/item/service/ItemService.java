package com.example.place.domain.item.service;

import com.example.place.common.annotation.Loggable;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.Image.dto.ImageDto;
import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.item.dto.ItemsAndIsFindByUserTag;
import com.example.place.domain.item.dto.request.ItemRequest;
import com.example.place.domain.item.dto.response.ItemResponse;
import com.example.place.domain.item.dto.response.ItemGetAllResponse;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.repository.ItemRepository;

import com.example.place.domain.keyword.service.SearchKeywordService;
import com.example.place.domain.tag.service.TagService;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.service.UserService;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLTransientException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final TagService tagService;
    private final UserService userService;
	private final ImageService imageService;
	private final SearchKeywordService searchKeywordService;

	@Retryable(
		retryFor = { CannotAcquireLockException.class, SQLTransientException.class, IllegalStateException.class },
		maxAttempts = 5,
		backoff = @Backoff(delay = 200) // ms
	)
    @Transactional
	@Loggable
    public ItemResponse createItem(Long userId, ItemRequest request) {
        User user = userService.findByIdOrElseThrow(userId);
		List<LocalDateTime> sales_start0_end1 = setSalesTime(request);

		boolean isAdminItem = user.getRole() == UserRole.ADMIN;

        Item item = Item.of(
                user,
				request.getItemName(),
				request.getItemDescription(),
				request.getPrice(),
				request.getCount(),
				isAdminItem,
				sales_start0_end1.get(0),
				sales_start0_end1.get(1)
				);
        itemRepository.save(item);

		// 연관 이미지 저장
		ImageDto imageDto = imageService.createImages(item, request.getImageUrls(), request.getMainIndex());

		// 연관 태그 저장
		tagService.saveTags(item, request.getItemTagNames());

		return ItemResponse.from(item, imageDto);
    }

	@Loggable
	@Transactional(readOnly = true)
	public ItemResponse getItem(Long itemId) {
		Item item = findByIdOrElseThrow(itemId);

		ImageDto imageDto = imageService.getItemImages(itemId);
		return ItemResponse.from(item, imageDto);
	}

	@Loggable
	@Transactional(readOnly = true)
	public PageResponseDto<ItemGetAllResponse> searchItems(String keyword, List<String> tags, Long userId,
		Pageable pageable) {

		searchKeywordService.addKeyword(keyword);

		Page<Item> pagedItems = itemRepository.search(keyword, tags, userId, pageable);

		return buildGetAllItems(pagedItems);
	}

	@Loggable
	@Transactional(readOnly = true)
	public ItemsAndIsFindByUserTag getAllItemsWIthUserTag(CustomPrincipal principal, Pageable pageable) {
		User user = userService.findByIdOrElseThrow(principal.getId());

		Page<Item> pagedItems = itemRepository.findByUserTag(user, pageable);
		boolean isFindByUserTag = true;

		// 조회 결과가 빈 페이지일 때
		if (pagedItems.isEmpty()) {
			pagedItems = itemRepository.findAllCustom(pageable);
			isFindByUserTag = false;
		}

		return ItemsAndIsFindByUserTag.of(buildGetAllItems(pagedItems), isFindByUserTag);
	}

	// 전체 조회 빌더 (공통 로직 메서드)
	@Transactional(readOnly = true)
	protected PageResponseDto<ItemGetAllResponse> buildGetAllItems(Page<Item> pagedItems) {
		// 해당 게시글 ID 목록에 대한 이미지 정보를 반환
		Map<Long, Image> mainImagesMap = imageService.getMainImagesForItems(pagedItems);

		// 조합
		Page<ItemGetAllResponse> dtoPage = pagedItems.map(item -> {
			// --메인이미지 조합
			Image mainImage = mainImagesMap.getOrDefault(item.getId(), null);

			return ItemGetAllResponse.from(item, mainImage.getImageUrl());
		});

		return new PageResponseDto<>(dtoPage);
	}

	@Transactional
	public ItemResponse updateItem(Long itemId, ItemRequest request, Long userId) {
		Item item = findByIdOrElseThrow(itemId);

		if(!item.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_ITEM_ACCESS);
		}
		item.updateItem(request);

		// 연관 이미지 수정
		if (((request.getImageUrls() == null || request.getImageUrls().isEmpty()) && request.getMainIndex() != null)
			|| (request.getImageUrls() != null && request.getMainIndex() == null)) {
			throw new CustomException(ExceptionCode.INVALID_IMAGE_UPDATE_REQUEST);
		}
		ImageDto imageDto = (request.getImageUrls() != null)
			? imageService.updateImages(item, request.getImageUrls(), request.getMainIndex())
			: imageService.getItemImages(itemId);

		// 연관 태그 수정
		if(request.getItemTagNames() != null) {
			item.getItemTags().clear();
			tagService.saveTags(item, request.getItemTagNames());
		}

		return ItemResponse.from(item, imageDto);
	}

	@Transactional
	public void deleteItem(Long itemId, Long userId) {

		if(!findByIdOrElseThrow(itemId).getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_ITEM_DELETE);
		}

		itemRepository.deleteById(itemId);
	}

	@Loggable
	@Transactional
	public void softDeleteItem(Long itemId, Long userId) {
		Item item = findByIdOrElseThrow(itemId);

		if (!findByIdOrElseThrow(itemId).getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_ITEM_DELETE);
		}

		item.delete();
	}

	/**
	 * 채팅을위해 상품의 판매자 조회
	 * @param itemId
	 * @return User
	 */
	public User getSeller(Long itemId) {
		Item item = findByIdOrElseThrow(itemId);
		return item.getUser();
	}

	public Item findByIdOrElseThrow(Long id) {
		Item item = itemRepository.findById(id)
				.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));

		if (item.isDeleted() == true) {
			throw new CustomException(ExceptionCode.NOT_FOUND_ITEM);
		}

		return item;
	}

	private List<LocalDateTime> setSalesTime(ItemRequest request) {

		LocalDateTime salesStartAt = request.getSalesStartAt() != null
				? request.getSalesStartAt()
				: LocalDateTime.now();

		LocalDateTime salesEndAt = request.getSalesEndAt() != null
				? request.getSalesEndAt()
				: LocalDateTime.of(3000, 1, 1, 0, 0);
		return List.of(salesStartAt, salesEndAt);
	}
}
