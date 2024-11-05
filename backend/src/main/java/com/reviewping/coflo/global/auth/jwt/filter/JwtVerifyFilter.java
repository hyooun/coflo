package com.reviewping.coflo.global.auth.jwt.filter;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.global.auth.jwt.utils.JwtConstants;
import com.reviewping.coflo.global.auth.jwt.utils.JwtProvider;
import com.reviewping.coflo.global.auth.oauth.service.AuthenticationService;
import com.reviewping.coflo.global.util.CookieUtils;
import com.reviewping.coflo.global.util.RedisUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtVerifyFilter extends OncePerRequestFilter {

    private final RedisUtil redisUtil;
    private final AuthenticationService authenticationService;

    private static final String[] whitelist = {
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/actuator/**",
        "/api/users/me",
        "/favicon.ico",
        "/webhook/*"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        log.info("요청받은 URI: " + requestURI);

        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String accessToken = CookieUtils.getCookieValue(request, JwtConstants.ACCESS_NAME);
        String refreshToken = CookieUtils.getCookieValue(request, JwtConstants.REFRESH_NAME);

        if (refreshToken != null) {
            handleRefreshToken(request, response, refreshToken);
        } else if (accessToken != null) {
            handleAccessToken(request, response, filterChain, accessToken);
        } else {
            proceedToNextFilter(request, response, filterChain, requestURI);
        }
    }

    private void handleAccessToken(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            String accessToken)
            throws IOException, ServletException {
        Map<String, Object> claims = JwtProvider.validateToken(accessToken);
        authenticationService.setAuthentication(((Integer) claims.get("userId")).longValue());

        filterChain.doFilter(request, response);
    }

    private void handleRefreshToken(
            HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        Map<String, Object> claims = JwtProvider.validateToken(refreshToken);
        String userId = (String) claims.get("id");
        String token = (String) redisUtil.get(userId);

        if (token != null || !refreshToken.equals(token)) {
            throw new JwtException(TOKEN_INVALID.getMessage());
        } else {
            redisUtil.delete(userId);
            String accessToken = JwtProvider.generateToken(claims, JwtConstants.ACCESS_EXP_TIME);
            refreshToken = JwtProvider.generateToken(claims, JwtConstants.REFRESH_EXP_TIME);
            redisUtil.set(userId, refreshToken, JwtConstants.REFRESH_EXP_TIME);

            Cookie accessTokenCookie =
                    CookieUtils.createCookie(
                            JwtConstants.ACCESS_NAME,
                            accessToken,
                            (int) JwtConstants.ACCESS_EXP_TIME / 1000);

            Cookie refreshTokenCookie =
                    CookieUtils.createCookie(
                            JwtConstants.REFRESH_NAME,
                            refreshToken,
                            (int) JwtConstants.REFRESH_EXP_TIME / 1000);

            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
        }
    }

    private void proceedToNextFilter(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            String requestURI)
            throws IOException, ServletException {
        if (PatternMatchUtils.simpleMatch(whitelist, requestURI)) {
            log.info("- 토큰이 없지만 허용된 경로입니다.");
            filterChain.doFilter(request, response);
        }

        throw new JwtException(TOKEN_INVALID.getMessage());
    }
}
