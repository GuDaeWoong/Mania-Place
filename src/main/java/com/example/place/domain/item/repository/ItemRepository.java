package com.example.place.domain.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.place.domain.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
