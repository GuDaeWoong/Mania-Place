package com.example.place.domain.order.entity;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;

public enum OrderStatus {
	// 주문완료
	ORDERED,
	// 택배 접수완료(판매자)
	READY,
	// 거래완료
	COMPLETED,
	// 거래취소
	CANCELLED;


	public static void statusIsReady(OrderStatus current) {
		if (current != ORDERED) {
			throw new CustomException(ExceptionCode.INVALID_ORDER_STATUS_TRANSITION);
		}
	}

	public static void statusIsCompleted(OrderStatus current) {
		if (current != READY) {
			throw new CustomException(ExceptionCode.INVALID_ORDER_STATUS_TRANSITION);
		}
	}
}
