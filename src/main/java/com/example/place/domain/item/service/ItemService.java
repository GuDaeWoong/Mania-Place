package com.example.place.domain.item.service;

import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.item.dto.request.ItemRequest;
import com.example.place.domain.item.dto.response.ItemResponse;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.repository.ItemRepository;
import com.example.place.domain.itemtag.entity.ItemTag;
import com.example.place.domain.itemtag.repository.ItemTagRepository;
import com.example.place.domain.tag.entity.Tag;
import com.example.place.domain.tag.repository.TagRepository;

import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.repository.UserRepository;
import com.example.place.domain.user.service.UserService;
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
    private final ItemTagRepository itemTagRepository;
    private final TagRepository tagRepository;
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

	//기본적인 태그 관리 흐름입니다
    @Transactional
    public ItemResponse createItem(Long userId, ItemRequest request) {
        User user = userService.findUserById(userId);

		LocalDateTime salesStartAt = request.getSalesStartAt() != null
				? request.getSalesStartAt()
				: LocalDateTime.now();

		LocalDateTime salesEndAt = request.getSalesEndAt() != null
				? request.getSalesEndAt()
				: LocalDateTime.of(3000, 1, 1, 0, 0);
//		:LocalDateTime.Max;
        Item item = Item.of(
                user,
				request.getItemName(),
				request.getItemDescription(),
				request.getPrice(),
				request.getCount(),
				salesStartAt,
				salesEndAt
				);
        itemRepository.save(item);

		// 연관 이미지 저장
		imageService.saveImages(item, request.getImages(), request.getMainIndex());

		//	태그 저장 로직
        for (String tagName: request.getItemTagNames()) {
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));

            ItemTag itemTag = new ItemTag(null, item, tag);

			item.addItemTag(itemTag);
        }
        return ItemResponse.from(item);
    }

	@Transactional(readOnly = true)
	public ItemResponse readItem(Long itemId) {
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
		if ((request.getImages() == null && request.getMainIndex() != null)
			|| (request.getImages() != null && request.getMainIndex() == null)) {
			throw new CustomException(ExceptionCode.INVALID_IMAGE_UPDATE_REQUEST);
		}

		if (request.getImages() != null && request.getMainIndex() != null) {
			imageService.updateImages(item, request.getImages(), request.getMainIndex());
		}

		return ItemResponse.from(item);
	}

	@Transactional
	public void deleteItem(Long itemId, Long userId) {

		if(!findByIdOrElseThrow(itemId).getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_ITEM_DELETE);
		}

		// 연관 이미지 삭제
		imageService.deleteImageByItemId(itemId);

		itemRepository.deleteById(itemId);
	}


	public Item findByIdOrElseThrow(Long id) {
		return itemRepository.findById(id)
				.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));
	}


	public List<ItemResponse> searchItem(String keyword, List<String> tags, Long userId) {
		return itemRepository.searchItems(keyword, tags, userId)
				.stream()
				.map(ItemResponse::from)
				.toList();
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
}
