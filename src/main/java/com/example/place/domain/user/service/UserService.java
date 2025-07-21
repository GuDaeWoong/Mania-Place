package com.example.place.domain.user.service;

import org.springframework.stereotype.Service;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User findUserById(Long userId){
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
		return user;
	}

}
