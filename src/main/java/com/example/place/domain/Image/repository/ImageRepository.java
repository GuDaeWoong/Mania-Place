package com.example.place.domain.Image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.place.domain.Image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByItemId(Long itemId);

	@Query("SELECT i FROM Image i WHERE i.item.id IN :itemIds")
	List<Image> findByItemIds(@Param("itemIds") List<Long> itemIds);
}
