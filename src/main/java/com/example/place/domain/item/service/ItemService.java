package com.example.place.domain.item.service;

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
import org.springframework.stereotype.Service;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;



import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemTagRepository itemTagRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

	public Item findItemById(Long itemId){
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));
		return item;
	}

	// 재고 감소
	@Transactional
	public void decreaseStock(Long itemId, int quantity) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));
		item.decreaseStock(quantity);
	}

	// 재고 증가
	@Transactional
	public void increaseStock(Long itemId, int quantity) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));
		item.increaseStock(quantity);
	}

	//기본적인 태그 관리 흐름입니다
    @Transactional
    public ItemResponse createItem(Long userId, ItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        Item item = new Item(
                user,
				request.getItemName(),
				request.getItemDescription(),
				request.getPrice(),
				request.getCount(),
				request.getSalesStartAt(),
				request.getSalesEndAt());
        itemRepository.save(item);

        for (String tagName: request.getItemTagNames()) {
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));

            ItemTag itemTag = new ItemTag(null, item, tag);
            itemTagRepository.save(itemTag);
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
		return ItemResponse.from(item);
	}

	@Transactional
	public void deleteItem(Long itemId, Long userId) {

		if(!findByIdOrElseThrow(itemId).getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_ITEM_DELETE);
		}
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
}
