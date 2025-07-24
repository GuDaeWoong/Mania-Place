package com.example.place.domain.item.repository;

import com.example.place.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT DISTINCT i FROM Item i " +
            "JOIN i.itemTags it " +
            "JOIN it.tag t " +
            "WHERE (:keyword IS NULL OR i.itemName LIKE %:keyword%)" +
            "AND (:userId IS NULL OR i.user.id = :userId)" +
            "AND (:tags IS NULL OR t.tagName IN :tags)")
    Page<Item> searchItems(
            @Param("keyword") String keyword,
            @Param("tags") List<String> tags,
            @Param("userId") Long userId,
            Pageable pageable
    );
}
