package com.example.place.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.place.domain.user.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(@Email @NotBlank String email);
}