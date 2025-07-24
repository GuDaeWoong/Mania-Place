package com.example.place.domain.order.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.place.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	Page<Order> findByUserId(Long userId, Pageable pageable);

	List<Order> findByItemId(Long itemId);
}
