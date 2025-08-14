package com.example.place.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);
	boolean existsByNickname(String nickname);
	Optional<User> findByEmail(String email);

	List<User> findByIsDeletedFalseAndRole(UserRole role);
}

