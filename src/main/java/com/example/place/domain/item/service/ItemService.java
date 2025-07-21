package com.example.place.domain.item.service;

import org.springframework.stereotype.Service;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.repository.ItemRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

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

}
