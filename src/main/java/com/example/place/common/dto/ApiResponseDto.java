package com.example.place.common.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"message", "data", "timestamp"})
public class ApiResponseDto<T> {

	private String message;

	private T data;

	private LocalDateTime timestamp;

	public ApiResponseDto(String message, T data) {
		this.message = message;
		this.data = data;
		this.timestamp = LocalDateTime.now();
	}
}
