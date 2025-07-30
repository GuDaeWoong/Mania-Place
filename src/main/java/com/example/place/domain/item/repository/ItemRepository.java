package com.example.place.domain.item.repository;

import com.example.place.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {


    @Query("""
            SELECT DISTINCT i FROM Item i
            JOIN FETCH i.itemTags it
            JOIN FETCH it.tag t
            WHERE (:keyword IS NULL OR i.itemName LIKE %:keyword%)
            AND (:userId IS NULL OR i.user.id = :userId)
            AND (:tags IS NULL OR t.tagName IN :tags)
            AND (:itemDescription IS NULL OR i.itemDescription LIKE %:itemDescription%)
        AND i.isDeleted = false
            """)
    List<Item> searchitems(
            @Param("keyword") String keyword,
            @Param("userId") Long userId,
            @Param("tags") List<String> tags,
            @Param("itemDescription") String itemDescription
    );
    //
}
