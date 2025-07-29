package com.example.place.domain.usertag.repository;

import java.util.List;

import com.example.place.domain.usertag.entity.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTagRepository extends JpaRepository<UserTag, Long> {
	@Modifying
	@Query(value = """
        INSERT INTO user_tags (user_id, tag_id)
        SELECT :userId, t.id
        FROM tags t
        WHERE t.id IN :tagIds
        """, nativeQuery = true)
	void bulkInsertUserTags(@Param("userId") Long userId, @Param("tagIds") List<Long> tagIds);
}
