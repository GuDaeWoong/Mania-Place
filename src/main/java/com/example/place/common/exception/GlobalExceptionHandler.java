package com.example.place.common.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.place.common.dto.ErrorResponseDto;
import com.example.place.common.exception.exceptionclass.CustomException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponseDto> customException(CustomException exception, HttpServletRequest request) {

		return customErrorResponse(exception.getExceptionCode().getHttpStatus(), exception.getMessage(),
			request.getRequestURI());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException e,
		HttpServletRequest request) {
		String errorMessage = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> error.getField() + ": " + error.getDefaultMessage())
			.collect(Collectors.joining(", "));

		log.warn("[ValidationException] {} | Path: {}", errorMessage, request.getRequestURI());

		return customErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, request.getRequestURI());
	}

	private ResponseEntity<ErrorResponseDto> customErrorResponse(HttpStatus status, String message, String path) {
		ErrorResponseDto errorResponseDto = new ErrorResponseDto(status.value(), status.getReasonPhrase(), message,
			path);
		return new ResponseEntity<>(errorResponseDto, status);
	}

}