package com.example.place.domain.user.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.tag.entity.Tag;
import com.example.place.domain.tag.repository.TagRepository;
import com.example.place.domain.user.dto.UserDeleteRequest;
import com.example.place.domain.user.dto.UserPasswordRequest;
import com.example.place.domain.user.dto.UserRegisterRequest;
import com.example.place.domain.user.dto.UserRegisterResponse;
import com.example.place.domain.user.dto.UserResponse;
import com.example.place.domain.user.dto.UserUpdateRequest;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.repository.UserRepository;
import com.example.place.domain.usertag.entity.UserTag;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TagRepository tagRepository;

	@Transactional
	public UserRegisterResponse register(UserRegisterRequest userRegisterRequest, UserRole userRole) {
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

		User user = User.of(
			userRegisterRequest.getName(),
			userRegisterRequest.getNickname(),
			userRegisterRequest.getEmail(),
			encodedPassword,
			userRegisterRequest.getImageUrl(),
			userRole
		);

		User savedUser = userRepository.save(user);

		saveTags(savedUser, userRegisterRequest.getTags());

		return new UserRegisterResponse(savedUser.getEmail());
	}

	// 어드민 검색용
	public UserResponse findUser(Long userId) {
		User foundUser = findByIdOrElseThrow(userId);
		return UserResponse.from(foundUser);
	}

	// 마이페이지 조회용
	public UserResponse findMyInfo(Long userId) {
		User myInfo = findByIdOrElseThrow(userId);
		return UserResponse.from(myInfo);
	}

	@Transactional
	public UserResponse modifyUser(Long userId, UserUpdateRequest userUpdateRequest) {
		User foundUser = findByIdOrElseThrow(userId);

		// 닉네임이 변경되었을 때만 중복 검증
		if (!foundUser.getNickname().equals(userUpdateRequest.getNickname()) &&
			userRepository.existsByNickname(userUpdateRequest.getNickname())) {
			throw new CustomException(ExceptionCode.EXISTS_NICKNAME);
		}

		foundUser.updateUserInfo(
			userUpdateRequest.getName(),
			userUpdateRequest.getNickname(),
			userUpdateRequest.getImageUrl());

		updateUserTags(foundUser, userUpdateRequest.getTags());

		return UserResponse.from(foundUser);
	}

	@Transactional
	public Void modifyPassword(Long userId, UserPasswordRequest userPasswordRequest) {
		User foundUser = findByIdOrElseThrow(userId);

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
		User foundUser = findByIdOrElseThrow(userId);

		// 비밀번호가 일치하는지 확인
		if (!passwordEncoder.matches(userDeleteRequest.getPassword(), foundUser.getPassword())) {
			throw new CustomException(ExceptionCode.INCORRECT_PASSWORD);
		}

		// Soft Delete
		foundUser.delete();

		return null;
	}

	// 태그 업데이트 메서드
	private void updateUserTags(User user, Set<String> newTagNames) {
		// 기존 태그 모두 제거 (orphanRemoval = true 설정되어 있다면 자동 삭제)
		user.getUserTags().clear();
		saveTags(user, newTagNames);
	}

	// 태그 저장 메서드
	private void saveTags(User user, Set<String> tagNames) {
		for (String tagName: tagNames) {
			Tag tag = tagRepository.findByTagName(tagName)
				.orElseGet(() -> tagRepository.save(Tag.of(tagName)));

			UserTag userTag = UserTag.of(tag, user);

			user.addUserTag(userTag);
		}
	}

	public User findByIdOrElseThrow(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

		// 탈퇴한 사용자인지 확인
		if(user.isDeleted()) {
			throw new CustomException(ExceptionCode.DELETED_USER);
		}

		return user;
	}

	public Optional<Object> findByEmailOrElseThrow(String email) {
		return Optional.ofNullable(userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER)));
	}
}
