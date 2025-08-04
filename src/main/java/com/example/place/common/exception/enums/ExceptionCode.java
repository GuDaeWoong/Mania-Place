package com.example.place.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionCode {

	// 400 Bad Request
	INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
	DUPLICATE_NEW_PASSWORD(HttpStatus.BAD_REQUEST, "새 비밀번호는 기존 비밀번호와 달라야 합니다."),
	PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호 확인이 일치하지 않습니다."),
	NOT_SALE_PERIOD(HttpStatus.BAD_REQUEST, "판매 기간이 아닙니다."),
	INVALID_ORDER_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "허용되지 않은 주문 상태 변환입니다."),
	INVALID_IMAGE_UPDATE_REQUEST(HttpStatus.BAD_REQUEST, "이미지 수정을 위해서는 이미지 URL 목록과 대표 이미지 인덱스를 함께 보내주세요"),
	INVALID_PATH(HttpStatus.BAD_REQUEST, "유효하지 않는 주소입니다."),
	TOO_MANY_TAGS(HttpStatus.BAD_REQUEST, "태그는 최대 10개까지만 입력이 가능합니다."),

	// 401 Unauthorized
	PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	INVALID_OR_MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다."),
	EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
	INVALID_EMAIL_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 이메일 또는 비밀번호입니다."),

	// 403 Forbidden
	LOGGED_OUT_TOKEN(HttpStatus.FORBIDDEN, "로그아웃 처리된 토큰입니다."),
	FORBIDDEN_ITEM_ACCESS(HttpStatus.FORBIDDEN, "본인 외에 상품은 수정할 수 없습니다."),
	FORBIDDEN_ITEM_DELETE(HttpStatus.FORBIDDEN, "본인 외에 상품은 삭제할 수 없습니다."),
	FORBIDDEN_POST_ACCESS(HttpStatus.FORBIDDEN, "본인 외의 게시글은 수정할 수 없습니다."),
	FORBIDDEN_POST_DELETE(HttpStatus.FORBIDDEN, "본인 외의 게시글은 삭제할 수 없습니다."),
	FORBIDDEN_NEWSFEED_ACCESS(HttpStatus.FORBIDDEN, "본인 외의 게시글은 수정할 수 없습니다."),
	FORBIDDEN_NEWSFEED_DELETE(HttpStatus.FORBIDDEN, "본인 외의 게시글은 삭제할 수 없습니다."),
	FORBIDDEN_ORDER_ACCESS(HttpStatus.FORBIDDEN, "해당 주문은 본인의 주문이 아닙니다."),
	ORDER_CANCEL_ACCESS_DENIED(HttpStatus.FORBIDDEN, "주문을 취소할 권한이 없습니다."),
	ITEM_HAS_ACTIVE_ORDERS(HttpStatus.FORBIDDEN, "아직 진행 중인 주문이 있어 취소할 수 없습니다."),
	FORBIDDEN_COMMENT_ACCESS(HttpStatus.FORBIDDEN, "본인 외에 댓글은 수정할 수 없습니다."),
	FORBIDDEN_COMMENT_DELETE(HttpStatus.FORBIDDEN, "본인 외에 댓글은 삭제할 수 없습니다."),
	DELETED_USER(HttpStatus.FORBIDDEN, "탈퇴한 사용자 입니다."),
	UNAUTHORIZED_STATUS_CHANGE(HttpStatus.FORBIDDEN, "본인 이외에는 상태값을 변경할 수 없습니다."),
	NOT_SELLER(HttpStatus.FORBIDDEN, "구매자가 아닙니다."),
	FORBIDDEN_CHAT_WITH_SELLF(HttpStatus.FORBIDDEN, "본인의 상품에는 채팅방을 생성할 수 없습니다."),

	// 404 Not Found
	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
	NOT_FOUND_ITEM(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."),
	NOT_FOUND_ITEM_COMMENT(HttpStatus.NOT_FOUND, "존재하지 않는 판매글입니다."),
	NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
	NOT_FOUND_POST(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."),
	NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "존재하지 않는 오더id 입니다."),
	NOT_FOUND_VOTE(HttpStatus.NOT_FOUND, "존재하지 않는 투표입니다."),
	NOT_FOUND_TAG(HttpStatus.NOT_FOUND, "존재하지 않는 태그입니다."),
	NOT_FOUND_NEWSFEED(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."),
	NOT_FOUND_CHATROOM(HttpStatus.NOT_FOUND,"존재하지 않는 채팅방입니다."),

	// 409 Conflict
	OUT_OF_STOCK(HttpStatus.CONFLICT, "상품의 개수가 부족합니다."),
	ALREADY_VOTED(HttpStatus.CONFLICT, "이미 투표한 게시글입니다."),
	EXISTS_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
	EXISTS_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
	BLACKLISTED_TOKEN(HttpStatus.CONFLICT, "다시 로그인 해주세요."),
	DUPLICATED_TAG_NAME(HttpStatus.CONFLICT, "이미 존재하는 태그명입니다."),

	// 동시성 제어 관련 예외
	STOCK_LOCK_FAILED(HttpStatus.CONFLICT, "재고 처리 중입니다. 잠시 후 다시 시도해주세요."),
	OPERATION_INTERRUPTED(HttpStatus.INTERNAL_SERVER_ERROR, "작업 처리 중 오류가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	ExceptionCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
