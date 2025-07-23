package com.example.place.domain.user.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.user.dto.UserDeleteRequest;
import com.example.place.domain.user.dto.UserPasswordRequest;
import com.example.place.domain.user.dto.UserRegisterRequest;
import com.example.place.domain.user.dto.UserRegisterResponse;
import com.example.place.domain.user.dto.UserResponse;
import com.example.place.domain.user.dto.UserUpdateRequest;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.repository.UserRepository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

	// 어드민 검색용
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

	// 마이페이지 조회용
	public UserResponse findMyInfo(Long userId) {
		User myInfo = findUserById(userId);
		return new UserResponse(
			myInfo.getId(),
			myInfo.getName(),
			myInfo.getNickname(),
			myInfo.getImageUrl(),
			myInfo.getEmail()
		);
	}

	@Transactional
	public UserResponse modifyUser(Long userId, UserUpdateRequest userUpdateRequest) {
		User foundUser = findUserById(userId);

		// 닉네임이 변경되었을 때만 중복 검증
		if (!foundUser.getNickname().equals(userUpdateRequest.getNickname()) &&
			userRepository.existsByNickname(userUpdateRequest.getNickname())) {
			throw new CustomException(ExceptionCode.EXISTS_NICKNAME);
		}

		foundUser.updateUserInfo(
			userUpdateRequest.getName(),
			userUpdateRequest.getNickname(),
			userUpdateRequest.getImageUrl());

		return new UserResponse(
			foundUser.getId(),
			foundUser.getName(),
			foundUser.getNickname(),
			foundUser.getImageUrl(),
			foundUser.getEmail()
		);
	}

	@Transactional
	public Void modifyPassword(Long userId, UserPasswordRequest userPasswordRequest) {
		User foundUser = findUserById(userId);

		// 이전 비밀번호가 맞는지 확인
		if (!passwordEncoder.matches(userPasswordRequest.getOldPassword(), foundUser.getPassword())) {
			throw new CustomException(ExceptionCode.INCORRECT_PASSWORD);
		}

		// 이전 비밀번호와 새 비밀번호가 동일한지 확인
		if(userPasswordRequest.getNewPassword().equals(userPasswordRequest.getOldPassword())){
			throw new CustomException(ExceptionCode.DUPLICATE_NEW_PASSWORD);
		}

		// 비밀번호 확인 일치 여부
		if(!userPasswordRequest.getNewPassword().equals(userPasswordRequest.getNewPasswordCheck())) {
			throw new CustomException(ExceptionCode.PASSWORD_CONFIRM_MISMATCH);
		}

		String encodedPassword = passwordEncoder.encode(userPasswordRequest.getNewPassword());

		foundUser.updateUserPassword(encodedPassword);

		return null;
	}

	@Transactional
	public Void deleteUser(Long userId, UserDeleteRequest userDeleteRequest) {
		User foundUser = findUserById(userId);

		// 비밀번호가 일치하는지 확인
		if (!passwordEncoder.matches(userDeleteRequest.getPassword(), foundUser.getPassword())) {
			throw new CustomException(ExceptionCode.INCORRECT_PASSWORD);
		}

		// Soft Delete
		foundUser.delete();

		return null;
	}

	public User findUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
	}

	public Optional<Object> findByEmail(String email) {
		return Optional.ofNullable(userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER)));
	}
}
