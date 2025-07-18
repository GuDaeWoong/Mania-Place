package com.example.place.common.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {

	private String message;
	private Object data;
	private LocalDateTime timestamp;

	// 성공 응답 static 메서드
	public static ApiResponseDto success(String message,Object data){
		return new ApiResponseDto(message , data, LocalDateTime.now());
	}

	// 실패 응답 static 메서드
	public static ApiResponseDto error(String message){
		return new ApiResponseDto(message, null, LocalDateTime.now());
	}
}
