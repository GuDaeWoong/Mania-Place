package com.example.place.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.place.domain.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
