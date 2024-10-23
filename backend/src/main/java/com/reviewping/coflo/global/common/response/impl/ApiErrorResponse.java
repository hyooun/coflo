package com.reviewping.coflo.global.common.response.impl;

import static com.reviewping.coflo.global.common.response.ResponseStatus.*;

import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.ResponseStatus;
import com.reviewping.coflo.global.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiErrorResponse<T> extends ApiResponse<T> {
    private String code;
    private String message;

    public ApiErrorResponse(ResponseStatus status, T data, String code, String message) {
        super(status, data);
        this.code = code;
        this.message = message;
    }

    public static ApiResponse<Void> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), null, errorCode.getMessage());
    }

    public static <T> ApiResponse<T> error(String code, T data, String message) {
        return new ApiErrorResponse<>(ERROR, data, code, message);
    }
}
