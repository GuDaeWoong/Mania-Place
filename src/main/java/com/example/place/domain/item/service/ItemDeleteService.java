package com.example.place.domain.item.service;

import org.springframework.stereotype.Service;

import com.example.place.domain.order.service.OrderService;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemDeleteService {

	private final OrderService orderService;
	private final ItemService itemService;

	@Transactional
	public void deleteItemAndclearItemPK(Long itemId, Long userId) {
		orderService.clearItemFromOrders(itemId);
		itemService.deleteItem(itemId, userId);
	}
}
