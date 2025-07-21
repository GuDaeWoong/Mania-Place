package com.example.place.domain.item.service;

import com.example.place.domain.item.dto.request.CreateItemRequest;
import com.example.place.domain.item.dto.response.CreateItemResponse;
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
    public CreateItemResponse createItem(CreateItemRequest dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 x"));

        Item item = new Item(
                user,
                dto.getItemName(),
                dto.getItemDescription(),
                dto.getPrice(),
                dto.getCount(),
                dto.getSalesStartAt(),
                dto.getSalesEndAt());
        itemRepository.save(item);

        for (String tagName: dto.getItemTagNames()) {
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));

            ItemTag itemTag = new ItemTag(null, item, tag);
            itemTagRepository.save(itemTag);
        }
        return new CreateItemResponse();
    }
}
