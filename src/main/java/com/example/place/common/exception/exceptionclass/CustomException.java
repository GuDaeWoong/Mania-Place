package com.example.place.common.exception.exceptionclass;

import com.example.place.common.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final ExceptionCode exceptionCode;

	public CustomException(ExceptionCode exceptionCode) {
		super(exceptionCode.getMessage());
		this.exceptionCode = exceptionCode;
	}

}