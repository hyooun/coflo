package com.reviewping.coflo.global.jwt.filter;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.global.jwt.utils.JwtConstants;
import com.reviewping.coflo.global.jwt.utils.JwtProvider;
import com.reviewping.coflo.global.util.RedisUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtVerifyFilter extends OncePerRequestFilter {

    private final RedisUtil redisUtil;

    private static final String[] whitelist = {
        "/api/swagger-ui/**", "/api/v3/**", "/api/users/me", "/favicon.ico"
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
        String accessToken = request.getHeader(JwtConstants.JWT_HEADER);
        String refreshToken = request.getHeader(JwtConstants.JWT_REFRESH_HEADER);

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
            String authHeader)
            throws IOException, ServletException {
        String token = JwtProvider.getTokenFromHeader(authHeader);
        Authentication authentication = JwtProvider.getAuthentication(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
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

            response.setHeader(JwtConstants.JWT_HEADER, accessToken);
            response.setHeader(JwtConstants.JWT_REFRESH_HEADER, refreshToken);
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
