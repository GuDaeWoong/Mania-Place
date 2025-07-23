package com.example.place.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.user.dto.UserDeleteRequest;
import com.example.place.domain.user.dto.UserPasswordRequest;
import com.example.place.domain.user.dto.UserRegisterRequest;
import com.example.place.domain.user.dto.UserRegisterResponse;
import com.example.place.domain.user.dto.UserResponse;
import com.example.place.domain.user.dto.UserUpdateRequest;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	//유저 회원가입
	@PostMapping
	public ResponseEntity<ApiResponseDto<UserRegisterResponse>> register(
		@Valid @RequestBody UserRegisterRequest userRegisterRequest
	) {
		return ResponseEntity.ok(new ApiResponseDto<>("회원가입이 완료되었습니다.", userService.register(userRegisterRequest, UserRole.USER)));
	}

	//어드민 회원가입
	@PostMapping("/admin")
	public ResponseEntity<ApiResponseDto<UserRegisterResponse>> registerAdmin(
		@Valid @RequestBody UserRegisterRequest userRegisterRequest
	) {
		return ResponseEntity.ok(new ApiResponseDto<>("어드민 가입이 완료되었습니다.", userService.register(userRegisterRequest, UserRole.ADMIN)));
	}

	//유저 조회 일단은 어드민용?
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDto<UserResponse>> getUser(
		@PathVariable Long id
	) {
		return ResponseEntity.ok(new ApiResponseDto<>("회원 조회 성공", userService.findUser(id)));
	}

	//마이페이지 조회
	@GetMapping("/me")
	public ResponseEntity<ApiResponseDto<UserResponse>> getMyAccount(
		@AuthenticationPrincipal CustomPrincipal principal
	) {
		Long userId = principal.getId();
		return ResponseEntity.ok(new ApiResponseDto<>("마이페이지 조회 성공", userService.findMyInfo(userId)));
	}

	//내 정보 수정
	@PatchMapping("/me")
	public ResponseEntity<ApiResponseDto<UserResponse>> updateUser(
		@AuthenticationPrincipal CustomPrincipal principal,
		@Valid @RequestBody UserUpdateRequest userUpdateRequest
	) {
		Long userId = principal.getId();
		return ResponseEntity.ok(new ApiResponseDto<>("회원 정보가 수정되었습니다.", userService.modifyUser(userId, userUpdateRequest)));
	}

	//내 비밀번호 수정
	@PatchMapping("/me/password")
	public ResponseEntity<ApiResponseDto<Void>> changePassword(
		@AuthenticationPrincipal CustomPrincipal principal,
		@Valid @RequestBody UserPasswordRequest userPasswordRequest
	) {
		Long userId = principal.getId();
		return ResponseEntity.ok(new ApiResponseDto<>("비밀번호가 수정되었습니다.", userService.modifyPassword(userId, userPasswordRequest)));
	}

	//회원 삭제
	@DeleteMapping("/me")
	public ResponseEntity<ApiResponseDto<Void>> deleteUser(
		@AuthenticationPrincipal CustomPrincipal principal,
		@Valid @RequestBody UserDeleteRequest userDeleteRequest
	) {
		Long userId = principal.getId();
		return ResponseEntity.ok(new ApiResponseDto<>("회원 탈퇴가 완료되었습니다.", userService.deleteUser(userId, userDeleteRequest)));
	}
}
