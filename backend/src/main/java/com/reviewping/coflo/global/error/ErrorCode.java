package com.reviewping.coflo.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	/**
	 * USER(A000)
	 */
	USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "A001", "존재하지 않는 유저입니다"),

	/**
	 * JWT Token(H000)
	 */
	TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "H001", "유효하지 않은 토큰입니다."),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "H002", "만료된 토큰입니다."),
	TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "H003", "지원되지 않는 토큰입니다."),
	TOKEN_WRONG(HttpStatus.UNAUTHORIZED, "H004", "잘못된 토큰 서명입니다."),


	/**
	 * 외부 API(Y000)
	 */
	EXTERNAL_API_NOT_FOUND(HttpStatus.NOT_FOUND, "Y001", "외부 API를 찾을 수 없습니다."),
	EXTERNAL_API_METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Y002", "허용되지 않은 HTTP 메서드입니다."),
	EXTERNAL_API_UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Y003", "지원되지 않는 미디어 타입입니다."),
	EXTERNAL_API_BAD_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "Y004", "잘못된 요청입니다."),
	EXTERNAL_API_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Y005", "서버 내부 오류가 발생했습니다."),

	/**
	 * Etc(Z000)
	 */
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Z009", "잘못된 파라미터가 포함되었습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
