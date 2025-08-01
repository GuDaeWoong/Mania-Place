package com.example.place.domain.Image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.place.domain.Image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByItemId(Long itemId);

	@Query("SELECT i FROM Image i WHERE i.item.id IN :itemIds AND i.isMain = true")
	List<Image> findMainImagesByItemIds(@Param("itemIds") List<Long> itemIds);

	//뉴스피드 전체 조회 이미지 리스트
	@Query("SELECT i FROM Image i WHERE i.newsfeed.id IN :newsfeedIds AND i.isMain = true")
	List<Image> findMainImagesByNewsfeedIds(@Param("newsfeedIds") List<Long> newsfeedIds);
}

