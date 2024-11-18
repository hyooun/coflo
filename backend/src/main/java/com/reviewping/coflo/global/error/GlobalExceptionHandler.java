package com.reviewping.coflo.global.error;

import static com.reviewping.coflo.global.error.ErrorCode.*;
import static java.util.stream.Collectors.*;

import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiErrorResponse;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ApiResponse<Map<String, Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        List<ErrorDetail> errors =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(
                                fieldError ->
                                        new ErrorDetail(
                                                fieldError.getField(),
                                                fieldError.getDefaultMessage()))
                        .collect(toList());

        Map<String, Object> data = new HashMap<>();
        data.put("errors", errors);
        return ApiErrorResponse.error(
                INVALID_PARAMETER.getCode(), data, INVALID_PARAMETER.getMessage());
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiErrorResponse.error(errorCode));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
