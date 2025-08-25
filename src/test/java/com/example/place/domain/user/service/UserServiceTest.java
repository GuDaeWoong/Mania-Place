package com.example.place.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.tag.service.TagService;
import com.example.place.domain.user.dto.UserDeleteRequest;
import com.example.place.domain.user.dto.UserPasswordRequest;
import com.example.place.domain.user.dto.UserRegisterRequest;
import com.example.place.domain.user.dto.UserRegisterResponse;
import com.example.place.domain.user.dto.UserResponse;
import com.example.place.domain.user.dto.UserUpdateRequest;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private TagService tagService;

	@InjectMocks
	private UserService userService;

	@Captor
	ArgumentCaptor<User> UserCaptor;

	@Test
	void 회원가입_성공() {
		// given
		UserRegisterRequest request = new UserRegisterRequest(
			"testName",
			"testNickname",
			"test@email.com",
			"testPassword123!",
			"testImg.jpg"
		);

		when(userRepository.existsByEmail(any())).thenReturn(false);
		when(userRepository.existsByNickname(any())).thenReturn(false);
		when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

		// when
		UserRegisterResponse response = userService.register(request, UserRole.USER);

		// then
		verify(passwordEncoder, times(1)).encode(request.getPassword());
		verify(userRepository, times(1)).save(UserCaptor.capture());
		assertEquals(UserRole.USER, UserCaptor.getValue().getRole());
		assertEquals(request.getEmail(), response.getEmail());
	}

	@Test
	void 회원가입_중복이메일_실패() {
		// given
		UserRegisterRequest request = new UserRegisterRequest(
			"testName",
			"testNickname",
			"test@email.com",
			"testPassword123!",
			"testImg.jpg"
		);

		when(userRepository.existsByEmail(any())).thenReturn(true);

		// when
		CustomException exception = assertThrows(CustomException.class, () ->
			userService.register(request, UserRole.USER));

		// then
		assertEquals(ExceptionCode.EXISTS_EMAIL, exception.getExceptionCode());
	}

	@Test
	void 회원가입_중복닉네임_실패() {
		// given
		UserRegisterRequest request = new UserRegisterRequest(
			"testName",
			"testNickname",
			"test@email.com",
			"testPassword123!",
			"testImg.jpg"
		);

		when(userRepository.existsByEmail(any())).thenReturn(false);
		when(userRepository.existsByNickname(any())).thenReturn(true);

		// when
		CustomException exception = assertThrows(CustomException.class, () ->
			userService.register(request, UserRole.USER));

		// then
		assertEquals(ExceptionCode.EXISTS_NICKNAME, exception.getExceptionCode());
	}

	@Test
	void 관리자_회원가입_성공() {
		// given
		UserRegisterRequest request = new UserRegisterRequest(
			"testName",
			"testNickname",
			"test@email.com",
			"testPassword123!",
			"testImg.jpg"
		);

		when(userRepository.existsByEmail(any())).thenReturn(false);
		when(userRepository.existsByNickname(any())).thenReturn(false);
		when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

		// when
		UserRegisterResponse response = userService.register(request, UserRole.ADMIN);

		// then
		verify(passwordEncoder, times(1)).encode(request.getPassword());
		verify(userRepository, times(1)).save(UserCaptor.capture());
		assertEquals(UserRole.ADMIN, UserCaptor.getValue().getRole());
		assertEquals(request.getEmail(), response.getEmail());
	}

	@Test
	void 회원_조회_성공() {
		// given
		User testUser = User.of(
			"testUser",
			"testNickname",
			"test@email.com",
			"encodedPassword",
			"test.url",
			UserRole.USER
		);

		when(userRepository.findById(any())).thenReturn(Optional.of(testUser));

		// when
		UserResponse response = userService.findUser(1L);

		// then
		assertEquals(testUser.getEmail(), response.getEmail());
	}

	@Test
	void 회원_조회_존재하지_않는_회원_실패() {
		// given
		when(userRepository.findById(any())).thenReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class, () ->
			userService.findUser(1L));

		// then
		assertEquals(ExceptionCode.NOT_FOUND_USER, exception.getExceptionCode());
	}

	@Test
	void 회원_조회_탈퇴한_회원_실패() {
		// given
		User deletedUser = mock(User.class);
		when(userRepository.findById(any())).thenReturn(Optional.of(deletedUser));
		when(deletedUser.isDeleted()).thenReturn(true);

		// when
		CustomException exception = assertThrows(CustomException.class, () ->
			userService.findUser(1L));

		// then
		assertEquals(ExceptionCode.DELETED_USER, exception.getExceptionCode());
	}

	@Test
	void 회원_조회_마이페이지_성공() {
		// given
		User testUser = User.of(
			"testUser",
			"testNickname",
			"test@email.com",
			"encodedPassword",
			"test.url",
			UserRole.USER
		);

		when(userRepository.findById(any())).thenReturn(Optional.of(testUser));

		// when
		UserResponse response = userService.findMyInfo(1L);

		// then
		assertEquals(testUser.getEmail(), response.getEmail());
	}

	@Test
	void 회원_정보_수정_성공() {
		// given
		User testUser = User.of(
			"testUser",
			"testNickname",
			"test@email.com",
			"encodedPassword",
			"test.url",
			UserRole.USER
		);

		UserUpdateRequest request = new UserUpdateRequest(
			"UpdateName",
			"UpdateNickname",
			"UpdateImg.jpg"
		);

		when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
		when(userRepository.existsByNickname(request.getNickname())).thenReturn(false);

		doNothing().when(tagService).saveTags(testUser, request.getTags());

		// when
		UserResponse response = userService.modifyUser(1L, request);

		// then
		assertEquals("UpdateName", response.getName());
		assertEquals("UpdateNickname", response.getNickname());
		assertEquals("UpdateImg.jpg", response.getImageUrl());
	}

	@Test
	void 회원_정보_수정_중복_닉네님_실패() {
		// given
		User testUser = User.of(
			"testUser",
			"testNickname",
			"test@email.com",
			"encodedPassword",
			"test.url",
			UserRole.USER
		);

		UserUpdateRequest request = new UserUpdateRequest(
			"UpdateName",
			"UpdateNickname",
			"UpdateImg.jpg"
		);

		when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
		when(userRepository.existsByNickname(any())).thenReturn(true);

		// when
		CustomException exception = assertThrows(CustomException.class, () ->
			userService.modifyUser(1L, request));

		// then
		assertEquals(ExceptionCode.EXISTS_NICKNAME, exception.getExceptionCode());
	}

	@Test
	void 비밀번호_수정_성공() {
		// given
		User testUser = User.of(
			"testUser",
			"testNickname",
			"test@email.com",
			"encodedPassword",
			"testImg.jpg",
			UserRole.USER
		);

		UserPasswordRequest request = new UserPasswordRequest(
			"Test1234!",          // 이전 비밀번호
			"newTest1234!",       // 새 비밀번호
			"newTest1234!"        // 새 비밀번호 확인
		);

		when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
		when(passwordEncoder.matches("Test1234!", "encodedPassword")).thenReturn(true);
		when(passwordEncoder.encode("newTest1234!")).thenReturn("encodedNewPassword");

		// when
		userService.modifyPassword(1L, request);

		// then
		assertEquals("encodedNewPassword", testUser.getPassword());
	}

	@Test
	void 비밀번호_수정_틀린_이전비밀번호_실패() {
		// given
		User testUser = User.of(
			"testUser",
			"testNickname",
			"test@email.com",
			"encodedPassword",
			"testImg.jpg",
			UserRole.USER
		);

		UserPasswordRequest request = new UserPasswordRequest(
			"wrongPassword",
			"newTest1234!",
			"newTest1234!"
		);

		when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
		when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

		// when
		CustomException exception = assertThrows(CustomException.class, () ->
			userService.modifyPassword(1L, request)
		);

		// then
		assertEquals(ExceptionCode.INCORRECT_PASSWORD, exception.getExceptionCode());
	}

	@Test
	void 비밀번호_수정_새비밀번호_이전과_동일_실패() {
		// given
		User testUser = User.of(
			"testUser",
			"testNickname",
			"test@email.com",
			"encodedPassword",
			"testImg.jpg",
			UserRole.USER
		);

		UserPasswordRequest request = new UserPasswordRequest(
			"Test1234!",
			"Test1234!",
			"Test1234!"
		);

		when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
		when(passwordEncoder.matches("Test1234!", "encodedPassword")).thenReturn(true);

		// when
		CustomException exception = assertThrows(CustomException.class, () ->
			userService.modifyPassword(1L, request)
		);

		// then
		assertEquals(ExceptionCode.DUPLICATE_NEW_PASSWORD, exception.getExceptionCode());
	}

	@Test
	void 비밀번호_수정_새비밀번호_확인불일치_실패() {
		// given
		User testUser = User.of(
			"testUser",
			"testNickname",
			"test@email.com",
			"encodedPassword",
			"testImg.jpg",
			UserRole.USER
		);

		UserPasswordRequest request = new UserPasswordRequest(
			"Test1234!",
			"newTest1234!",
			"newTest1234Wrong!"
		);

		when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
		when(passwordEncoder.matches("Test1234!", "encodedPassword")).thenReturn(true);

		// when
		CustomException exception = assertThrows(CustomException.class, () ->
			userService.modifyPassword(1L, request)
		);

		// then
		assertEquals(ExceptionCode.PASSWORD_CONFIRM_MISMATCH, exception.getExceptionCode());
	}

	@Test
	void 회원_삭제_성공() {
		// given
		User testUser = User.of(
			"testUser",
			"testNickname",
			"test@email.com",
			"encodedPassword",
			"testImg.jpg",
			UserRole.USER
		);

		UserDeleteRequest request = new UserDeleteRequest("Test1234!");

		when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
		when(passwordEncoder.matches("Test1234!", "encodedPassword")).thenReturn(true);

		// when
		userService.deleteUser(1L, request);

		// then
		assertTrue(testUser.isDeleted());
	}

	@Test
	void 회원_삭제_비밀번호_불일치_실패() {
		// given
		User testUser = User.of(
			"testUser",
			"testNickname",
			"test@email.com",
			"encodedPassword",
			"testImg.jpg",
			UserRole.USER
		);

		UserDeleteRequest request = new UserDeleteRequest("wrongPassword");

		when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
		when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

		// when
		CustomException exception = assertThrows(CustomException.class, () ->
			userService.deleteUser(1L, request)
		);

		// then
		assertEquals(ExceptionCode.INCORRECT_PASSWORD, exception.getExceptionCode());
	}
}