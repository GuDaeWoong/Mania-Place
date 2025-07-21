package com.example.place.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.user.dto.UserRegisterRequest;
import com.example.place.domain.user.dto.UserRegisterResponse;
import com.example.place.domain.user.dto.UserResponse;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserRegisterResponse register(UserRegisterRequest userRegisterRequest) {
		// 이메일 중복 검증
		if (userRepository.existsByEmail(userRegisterRequest.getEmail())) {
			throw new CustomException(ExceptionCode.EXISTS_EMAIL);
		}
		// 닉네임 중복 검증
		if (userRepository.existsByNickname(userRegisterRequest.getNickname())) {
			throw new CustomException(ExceptionCode.EXISTS_NICKNAME);
		}
		// 패스워드 암호화
		String encodedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());

		User user = new User(
			userRegisterRequest.getName(),
			userRegisterRequest.getNickname(),
			userRegisterRequest.getEmail(),
			encodedPassword,
			userRegisterRequest.getImageUrl(),
			UserRole.USER
		);

		User savedUser = userRepository.save(user);

		return new UserRegisterResponse(savedUser.getEmail());
	}

	public UserResponse findUser(Long userId) {
		User foundUser = findUserById(userId);
		return new UserResponse(
			foundUser.getId(),
			foundUser.getName(),
			foundUser.getNickname(),
			foundUser.getImageUrl(),
			foundUser.getEmail()
		);
	}

	public User findUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
	}
}
