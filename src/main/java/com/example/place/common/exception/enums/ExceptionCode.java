package com.example.place.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionCode {

	// 400 Bad Request
	INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "비밀번호는 8자 이상이어야 하고, 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다."),
	INVALID_EMAIL_OR_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호 형식이 올바르지 않습니다."),
	INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
	DUPLICATE_NEW_PASSWORD(HttpStatus.BAD_REQUEST, "새 비밀번호는 기존 비밀번호와 달라야 합니다."),
	PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호 확인이 일치하지 않습니다."),
	REQUIRED_FIELD_MISSING(HttpStatus.BAD_REQUEST, "필수 항목에 값을 입력해주세요."),
	PASSWORD_TOO_SHORT(HttpStatus.BAD_REQUEST, "비밀번호는 최소 4자 이상이어야 합니다."),
	CONTENT_REQUIRED(HttpStatus.BAD_REQUEST, "내용을 입력해주세요."),
	ADDRESS_REQUIRED(HttpStatus.BAD_REQUEST, "주소지가 입력되지 않았습니다."),
	NOT_SALE_PERIOD(HttpStatus.BAD_REQUEST, "판매 기간이 아닙니다."),


	// 401 Unauthorized
	LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
	PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	INVALID_OR_MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰 정보입니다."),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료됐습니다."),
	INVALID_EMAIL_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 이메일 또는 비밀번호입니다."),

	// 403 Forbidden
	LOGGED_OUT_TOKEN(HttpStatus.FORBIDDEN, "로그아웃 처리된 토큰입니다."),
	FORBIDDEN_ITEM_ACCESS(HttpStatus.FORBIDDEN, "본인 외에 상품은 수정할 수 없습니다."),
	FORBIDDEN_ITEM_DELETE(HttpStatus.FORBIDDEN, "본인 외에 상품은 삭제할 수 없습니다."),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "관리자가 아니므로 수정할 수 없습니다."),


	// 404 Not Found
	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
	NOT_FOUND_ITEM(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."),
	NOT_FOUND_ITEM_COMMENT(HttpStatus.NOT_FOUND, "존재하지 않는 판매글입니다."),
	NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
	NOT_FOUND_POST(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."),
	NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "존재하지 않는 오더id 입니다."),



	// 409 Conflict
	OUT_OF_STOCK(HttpStatus.CONFLICT, "상품의 개수가 부족합니다."),
	ALREADY_VOTED(HttpStatus.CONFLICT, "이미 투표한 게시글입니다."),
	EXISTS_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");

	private final HttpStatus httpStatus;
	private final String message;

	ExceptionCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
