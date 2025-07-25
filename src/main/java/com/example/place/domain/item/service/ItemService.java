package com.example.place.domain.item.service;

import com.example.place.common.dto.PageResponseDto;
import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.item.dto.request.ItemRequest;
import com.example.place.domain.item.dto.response.ItemResponse;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.repository.ItemRepository;

import com.example.place.domain.tag.service.TagService;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;



import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final TagService tagService;
    private final UserService userService;
	private final ImageService imageService;


	// 재고 감소
	@Transactional
	public void decreaseStock(Long itemId, Long quantity) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));
		item.decreaseStock(quantity);
	}

	// 재고 증가
	@Transactional
	public void increaseStock(Long itemId, Long quantity) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));
		item.increaseStock(quantity);
	}

    @Transactional
    public ItemResponse createItem(Long userId, ItemRequest request) {
        User user = userService.findByIdOrElseThrow(userId);
		List<LocalDateTime> sales_start0_end1 = setSalesTime(request);

        Item item = Item.of(
                user,
				request.getItemName(),
				request.getItemDescription(),
				request.getPrice(),
				request.getCount(),
				sales_start0_end1.get(0),
				sales_start0_end1.get(1)
				);
        itemRepository.save(item);
		// 연관 이미지 저장
		imageService.createImages(item, request.getImageUrls(), request.getMainIndex());
		tagService.saveTags(item, request.getItemTagNames());

		return ItemResponse.from(item);
    }

	@Transactional(readOnly = true)
	public ItemResponse getItem(Long itemId) {
		Item item = findByIdOrElseThrow(itemId);
		return ItemResponse.from(item);
	}

	@Transactional
	public ItemResponse updateItem(Long itemId, ItemRequest request, Long userId) {
		Item item = findByIdOrElseThrow(itemId);

		if(!item.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_ITEM_ACCESS);
		}
		item.updateItem(request);

		// 연관 이미지 수정
		if ((request.getImageUrls() == null && request.getMainIndex() != null)
			|| (request.getImageUrls() != null && request.getMainIndex() == null)) {
			throw new CustomException(ExceptionCode.INVALID_IMAGE_UPDATE_REQUEST);
		}

		if (request.getImageUrls() != null) {
			imageService.updateImages(item, request.getImageUrls(), request.getMainIndex());
		}

		return ItemResponse.from(item);
	}

	@Transactional
	public void deleteItem(Long itemId, Long userId) {

		if(!findByIdOrElseThrow(itemId).getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_ITEM_DELETE);
		}

		itemRepository.deleteById(itemId);
	}

	@Transactional
	public PageResponseDto<ItemResponse> searchItems(String keyword, List<String> tags, Long userId, Pageable pageable) {
		List<Item> items = itemRepository.searchitems(keyword, userId, tags);

		int total = items.size();
		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), total);

		List<Item> pagedItems = items.subList(start, end);

		List<ItemResponse> itemResponses = pagedItems.stream()
				.map(ItemResponse::from)
				.toList();
		Page<ItemResponse> page = new PageImpl<>(itemResponses, pageable, total);

		return new PageResponseDto<>(page);
	}

	public Item findByIdOrElseThrow(Long id) {
		return itemRepository.findById(id)
				.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));
	}



	public String getMainImageUrl(Long itemId) {
		Item item =  itemRepository.findById(itemId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));

		return item.getImages().stream()
			.filter(Image::isMain)
			.findFirst()
			.map(Image::getImageUrl)
			.orElse(null);
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
