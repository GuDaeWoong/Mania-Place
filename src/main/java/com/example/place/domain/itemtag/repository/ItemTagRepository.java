package com.example.place.domain.itemtag.repository;

import com.example.place.domain.itemtag.entity.ItemTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemTagRepository extends JpaRepository<ItemTag, Long> {
}
