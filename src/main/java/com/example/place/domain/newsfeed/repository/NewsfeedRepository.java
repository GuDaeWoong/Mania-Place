package com.example.place.domain.newsfeed.repository;

import com.example.place.domain.newsfeed.entity.Newsfeed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {
}