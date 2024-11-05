package com.reviewping.coflo.global.error.exception;

import com.reviewping.coflo.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(String message, ErrorCode errorCode) {
        super(message + " : " + errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public BusinessException(String message, ErrorCode errorCode, Throwable cause) {
        super(message + " : " + errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
