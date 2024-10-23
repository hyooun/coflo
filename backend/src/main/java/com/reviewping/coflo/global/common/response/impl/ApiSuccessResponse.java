package com.reviewping.coflo.global.common.response.impl;

import static com.reviewping.coflo.global.common.response.ResponseStatus.*;

import com.reviewping.coflo.global.common.response.ApiResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiSuccessResponse<T> extends ApiResponse<T> {

    public ApiSuccessResponse(T data) {
        super(SUCCESS, data);
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiSuccessResponse<>(data);
    }
}
