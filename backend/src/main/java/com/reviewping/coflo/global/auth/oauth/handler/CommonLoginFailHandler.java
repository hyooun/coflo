package com.reviewping.coflo.global.auth.oauth.handler;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.util.WebHookUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Slf4j
@RequiredArgsConstructor
public class CommonLoginFailHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
        log.info("=== 로그인 실패 === Exception: {}", exception.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorCode errorCode = ErrorCode.LOGIN_FAIL;

        Map<String, String> error =
                Map.of(
                        "status", "ERROR",
                        "code", errorCode.getCode(),
                        "message", errorCode.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(error));

        String logMessage =
                String.format("OAuth2 Authentication Failed: %s", exception.getMessage());
        WebHookUtils.sendWebHookMessage(logMessage);
    }
}
