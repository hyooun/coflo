package com.reviewping.coflo.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /**
     * USER(A000)
     */
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "A001", "존재하지 않는 유저입니다"),
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "A002", "로그인 도중 오류가 발생했습니다."),
    USER_TOKEN_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "A003", "이미 등록된 USER TOKEN입니다."),
    USER_AUTHORIZATION_NOT_EXIST(HttpStatus.UNAUTHORIZED, "A004", "권한이 없습니다."),

    /**
     * MR_INFO(B000)
     */
    MR_INFO_NOT_EXIST(HttpStatus.NOT_FOUND, "B001", "MR_INFO를 찾을 수 없습니다."),

    /**
     * Gitlab(C000)
     */
    USER_GITLAB_ACCOUNT_NOT_EXIST(HttpStatus.NOT_FOUND, "C001", "사용자의 깃랩 계정이 존재하지 않습니다."),
    GITLAB_REQUEST_SERIALIZATION_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR, "C002", "GITLAB 요청 생성에 오류가 발생했습니다."),
    UNSUPPORTED_WEBHOOK_ACTION(HttpStatus.NOT_FOUND, "C003", "지원하지 않는 MR 웹훅 요청입니다."),
    UNSUPPORTED_EVENT_TYPE(HttpStatus.NOT_FOUND, "C004", "지원하지 않는 웹훅 이벤트입니다."),
    GITLAB_URL_PARSE_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR, "C005", "웹훅 REQUEST에서 URL 파싱 중 에러가 발생했습니다."),
    GITLAB_EVENT_REQUEST_SERIALIZATION_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR, "C006", "웹훅 REQUEST 역직렬화 중 에러가 발생했습니다."),
    /**
     * REVIEW(D000)
     */
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "D001", "존재하지 않는 리뷰입니다."),
    UNSUPPORTED_LANGUAGE(HttpStatus.INTERNAL_SERVER_ERROR, "D002", "지원하지 않는 언어입니다."),

    /**
     * JWT TOKEN(E000)
     */
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "E001", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "E002", "만료된 토큰입니다."),
    TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "E003", "지원되지 않는 토큰입니다."),
    TOKEN_WRONG(HttpStatus.UNAUTHORIZED, "E004", "잘못된 토큰 서명입니다."),
    TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, "E005", "토큰이 존재하지 않습니다."),

    /**
     * CRYPTO(F000)
     */
    ENCRYPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "F001", "암호화 도중 오류가 발생했습니다."),
    DECRYPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "F002", "복호화 도중 오류가 발생했습니다."),

    /**
     * CUSTOM PROMPT(G000)
     */
    CUSTOM_PROMPT_NOT_EXIST(HttpStatus.NOT_FOUND, "G001", "존재하지 않는 커스텀 프롬프트입니다."),

    /**
     * PROJECT(H000)
     */
    PROJECT_NOT_EXIST(HttpStatus.NOT_FOUND, "H001", "존재하지 않는 프로젝트입니다."),
    PROJECT_CALCULATION_TYPE_NOT_EXIST(
            HttpStatus.BAD_REQUEST, "H002", "잘못된 CalculationType 파라미터 값입니다."),
    PROJECT_SCORE_DISPLAY_TYPE_NOT_EXIST(
            HttpStatus.BAD_REQUEST, "H003", "잘못된 ScoreDisplayType 파라미터 값입니다."),

    /**
     * USER PROJECT(I000)
     */
    LINK_BOT_TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "I001", "연동에 필요한 Bot 토큰이 존재하지 않습니다."),
    USER_PROJECT_NOT_EXIST(HttpStatus.NOT_FOUND, "I002", "연동되지 않는 프로젝트입니다."),

    /**
     * WEBHOOK CHANNEL(J000)
     */
    CHANNEL_CODE_NOT_EXIST(HttpStatus.NOT_FOUND, "J001", "존재하지 않는 채널 코드입니다."),
    WEBHOOK_REQUEST_SERIALIZATION_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR, "J002", "Webhook Content 요청 생성에 오류가 발생했습니다."),
    WEBHOOK_CHANNEL_NOT_EXIST(HttpStatus.NOT_FOUND, "J003", "존재하지 않는 웹훅 채널입니다."),

    /**
     * BADGE(K000)
     */
    USER_BADGE_NOT_EXIST(HttpStatus.NOT_FOUND, "K001", "존재하지 않는 사용자 뱃지입니다."),
    BADGE_NOT_EXIST(HttpStatus.NOT_FOUND, "K002", "존재하지 않는 뱃지입니다."),

    /**
     * language (L000)
     */
    LANGUAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "L001", "지원하지 않는 언어입니다."),

    /**
     * branch (M000)
     */
    BRANCH_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 브랜치입니다."),

    /**
     * EXTERNAL API(Y000)
     */
    EXTERNAL_API_NOT_FOUND(HttpStatus.NOT_FOUND, "Y001", "외부 API를 찾을 수 없습니다."),
    EXTERNAL_API_METHOD_NOT_ALLOWED(
            HttpStatus.METHOD_NOT_ALLOWED, "Y002", "외부 API 요청에 허용되지 않은 HTTP 메서드입니다."),
    EXTERNAL_API_UNSUPPORTED_MEDIA_TYPE(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Y003", "외부 API 요청에 지원되지 않는 미디어 타입입니다."),
    EXTERNAL_API_BAD_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "Y004", "잘못된 외부 API 요청입니다."),
    EXTERNAL_API_INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR, "Y005", "외부 API 서버 내부 오류가 발생했습니다."),
    EXTERNAL_API_UNAUTHORIZED(HttpStatus.BAD_REQUEST, "Y006", "외부 API 토큰이 유효하지 않습니다."),
    EXTERNAL_API_COMMUNICATION(HttpStatus.SERVICE_UNAVAILABLE, "Y007", "외부 API와의 통신 중 오류가 발생했습니다."),
    EXTERNAL_API_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "Y008", "외부 API 요청이 시간 초과되었습니다."),

    /**
     * ETC(Z000)
     */
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Z009", "잘못된 파라미터가 포함되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
