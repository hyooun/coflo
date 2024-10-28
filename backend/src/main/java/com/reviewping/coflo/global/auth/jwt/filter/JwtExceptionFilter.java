package com.reviewping.coflo.global.auth.jwt.filter;

import static com.reviewping.coflo.global.error.ErrorCode.*;
import static org.springframework.util.MimeTypeUtils.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.global.error.ErrorCode;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            if (!response.isCommitted()) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");

                ErrorCode errorCode = determineErrorCode(e);

                Map<String, String> error =
                        Map.of(
                                "status", "ERROR",
                                "code", errorCode.getCode(),
                                "message", errorCode.getMessage());
                try {
                    response.getWriter().write(objectMapper.writeValueAsString(error));
                } catch (IllegalStateException ex) {
                    log.error("Response output stream already used", ex);
                }
            }
        }
    }

    private ErrorCode determineErrorCode(Exception e) {
        if (e instanceof JwtException) {
            String errorMessage = e.getMessage();

            if (errorMessage.contains(TOKEN_EXPIRED.getMessage())) {
                return TOKEN_EXPIRED;
            } else if (errorMessage.contains(TOKEN_UNSUPPORTED.getMessage())) {
                return TOKEN_UNSUPPORTED;
            } else if (errorMessage.contains(USER_NOT_EXIST.getMessage())) {
                return USER_NOT_EXIST;
            }
        }

        return TOKEN_INVALID;
    }
}
