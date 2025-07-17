package com.example.place.common.exception;

import javax.lang.model.type.ErrorType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

	private final ErrorType errorType;

}