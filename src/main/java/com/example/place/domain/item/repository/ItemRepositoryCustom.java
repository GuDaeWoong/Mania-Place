package com.example.place.domain.item.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.place.domain.item.entity.Item;
import com.example.place.domain.user.entity.User;

public interface ItemRepositoryCustom {
	Page<Item> findByUserTag(User user, Pageable pageable);

	Page<Item> search(String keyword, List<String> tags, Long userId, Pageable pageable);
}
