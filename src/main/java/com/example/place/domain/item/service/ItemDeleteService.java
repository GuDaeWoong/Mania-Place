package com.example.place.domain.item.service;

import org.springframework.stereotype.Service;

import com.example.place.domain.itemcomment.service.ItemCommentService;
import com.example.place.domain.order.service.OrderService;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemDeleteService {

	private final OrderService orderService;
	private final ItemService itemService;
	private final ItemCommentService itemCommentService;

	@Transactional
	public void removeReferencesAndDeleteItem(Long itemId, Long userId) {
		orderService.removeItemReferenceFromOrders(itemId);
		itemService.softDeleteItem(itemId, userId);
		itemCommentService.softDeleteAllItemComment(itemId);
	}
}
