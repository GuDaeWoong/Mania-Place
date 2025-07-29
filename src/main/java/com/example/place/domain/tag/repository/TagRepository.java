package com.example.place.domain.tag.repository;

import com.example.place.domain.tag.entity.Tag;
import com.example.place.domain.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagName(String tagName);
    boolean existsByTagName(String tagName);

    @Modifying
    @Query("DELETE FROM UserTag ut WHERE ut.user = :user")
    void deleteAllByUser(@Param("user") User user);

    List<Tag> findByTagNameIn(Set<String> normalizedTags);
}
