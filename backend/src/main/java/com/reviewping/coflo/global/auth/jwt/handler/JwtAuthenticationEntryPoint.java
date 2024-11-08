package com.reviewping.coflo.global.auth.jwt.handler;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.global.common.response.impl.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401 에러 리턴
        setErrorResponse(request, response, authException);
    }

    private void setErrorResponse(
            HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response.getWriter()
                    .write(
                            objectMapper.writeValueAsString(
                                    ApiErrorResponse.error(TOKEN_NOT_EXIST)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
