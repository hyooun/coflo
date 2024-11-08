package com.reviewping.coflo.global.auth.jwt.handler;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.global.common.response.impl.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {
        // 필요한 권한 없이 접근하려 할때 403 리턴
        setErrorResponse(request, response, accessDeniedException);
    }

    private void setErrorResponse(
            HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response.getWriter()
                    .write(
                            objectMapper.writeValueAsString(
                                    ApiErrorResponse.error(USER_AUTHORIZATION_NOT_EXIST)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
