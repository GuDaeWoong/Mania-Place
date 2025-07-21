package com.example.place.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.place.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
