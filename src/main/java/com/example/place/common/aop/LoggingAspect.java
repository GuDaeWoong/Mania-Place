package com.example.place.common.aop;

import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
	private final ObjectMapper objectMapper;

	// 로그에서 마스킹 처리할 민감한 필드 이름들
	private static final Set<String> SENSITIVE_FIELDS = Set.of(
		"email", "password", "accessToken", "refreshToken",
		"authorization", "secret", "key"
	);

	public LoggingAspect(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Pointcut("@annotation(com.example.place.common.annotation.Loggable)")
	public void loggableMethods() {}

	@Around("loggableMethods()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		try {
			// 실제 대상 메서드 실행
			Object result = joinPoint.proceed();
			long duration = System.currentTimeMillis() - start; // 수행 시간 계산

			// 요청 정보 추출 (HTTP 메서드, URI, 사용자 ID 등)
			RequestInfo requestInfo = captureRequestInfo();

			// 결과 객체를 JSON으로 직렬화하고 민감 데이터 마스킹 처리
			String responseBody = serializeAndMaskResponse(result);

			// 성공 로그 기록
			logSuccess(requestInfo, responseBody, duration);
			return result;

		} catch (Exception e) {
			long duration = System.currentTimeMillis() - start;

			// 요청 정보 다시 추출 (예외 상황이라도 정보 로깅 시도)
			RequestInfo requestInfo = captureRequestInfo();
			// 실패 로그 기록
			logFailure(requestInfo, duration, e);
			throw e; // 예외 재던지기
		}
	}

	// 현재 HTTP 요청으로부터 요청 정보를 안전하게 추출하는 메서드
	private RequestInfo captureRequestInfo() {
		try {
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

			// 요청이 없거나 비동기 호출인 경우 빈 기본값 반환
			if (attrs == null) {
				return RequestInfo.empty();
			}

			HttpServletRequest req = attrs.getRequest();
			String method = req.getMethod(); // HTTP 메서드(GET, POST 등)
			String uri = req.getRequestURI(); // 요청 URI
			String userEmail = extractUserEmail(req); // 사용자 식별 정보 추출

			return RequestInfo.of(method, uri, userEmail);

		} catch (Exception e) {
			log.warn("Request 정보 추출 실패: {}", e.getMessage());
			return RequestInfo.empty(); // 실패 시 기본값 반환
		}
	}

	// 사용자 인증 정보에서 사용자 email을 추출하는 메서드
	private String extractUserEmail(HttpServletRequest request) {
		if (request.getUserPrincipal() != null) {
			return request.getUserPrincipal().getName();
		}
		return "anonymous";
	}

	// 응답 객체를 JSON 문자열로 직렬화하고 민감정보를 마스킹 처리하는 메서드
	private String serializeAndMaskResponse(Object response) {
		if (response == null) {
			return "null";
		}

		try {
			String json = objectMapper.writeValueAsString(response);
			return maskSensitiveData(json);
		} catch (Exception e) {
			log.warn("Response 직렬화 실패: {}", e.getMessage());
			return "Response 직렬화 실패";
		}
	}

	// JSON 문자열 내 민감한 필드값을 마스킹 처리하는 메서드
	private String maskSensitiveData(String json) {
		if (json == null) {
			return null;
		}

		String masked = json;
		for (String field : SENSITIVE_FIELDS) {
			// 대소문자 구분 없이 SENSITIVE_FIELDS를 ***로 교체
			masked = masked.replaceAll(
				"(?i)(\"" + field + "\"\\s*:\\s*\")([^\"]+)(\")",
				"$1***$3"
			);
		}
		return masked;
	}

	// 성공 로그 출력용 메서드
	private void logSuccess(RequestInfo requestInfo, String responseBody, long durationMs) {
		log.info("[{}] {} - {} ({} ms) | Response: {}",
			requestInfo.method(), requestInfo.uri(), requestInfo.userId(), durationMs, responseBody);
	}

	// 실패 로그 출력용 메서드
	private void logFailure(RequestInfo requestInfo, long durationMs, Exception e) {
		log.error("[{}] {} - {} ({} ms) - ERROR: {}",
			requestInfo.method(), requestInfo.uri(), requestInfo.userId(), durationMs, e.getMessage());
	}

	// 요청 정보 데이터를 불변으로 캡슐화하는 record 클래스
	private record RequestInfo(String method, String uri, String userId) {
		public static RequestInfo of(String method, String uri, String userId) {
			return new RequestInfo(method, uri, userId);
		}

		public static RequestInfo empty() {
			// 요청 정보를 알 수 없는 경우 기본값 반환
			return new RequestInfo("UNKNOWN", "UNKNOWN", "UNKNOWN");
		}
	}
}