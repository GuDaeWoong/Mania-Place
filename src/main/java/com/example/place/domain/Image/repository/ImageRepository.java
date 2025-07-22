package com.example.place.domain.Image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.place.domain.Image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByItemId(Long itemId);
}
