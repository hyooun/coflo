package com.reviewping.coflo.global.oauth.handler;

import com.reviewping.coflo.domain.user.entity.PrincipalDetail;
import com.reviewping.coflo.global.jwt.utils.JwtConstants;
import com.reviewping.coflo.global.jwt.utils.JwtProvider;
import com.reviewping.coflo.global.util.RedisUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class CommonLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${redirect-regist-url}")
    private String registUrl;

    @Value("${redirect-main-url}")
    private String mainUrl;

    private final RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
        Long userId = principal.getUserId();
        String username = principal.getUsername();

        log.info("=== 로그인 성공 ===");
        Map<String, Object> responseMap = Map.of("userId", principal.getUserId());
        String accessToken = JwtProvider.generateToken(responseMap, JwtConstants.ACCESS_EXP_TIME);
        String refreshToken = JwtProvider.generateToken(responseMap, JwtConstants.REFRESH_EXP_TIME);

        Cookie accessTokenCookie =
                createCookie(
                        JwtConstants.JWT_HEADER,
                        accessToken,
                        (int) JwtConstants.ACCESS_EXP_TIME / 1000,
                        true,
                        false,
                        "/");
        Cookie refreshTokenCookie =
                createCookie(
                        JwtConstants.JWT_REFRESH_HEADER,
                        refreshToken,
                        (int) JwtConstants.REFRESH_EXP_TIME / 1000,
                        true,
                        false,
                        "/");

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        redisUtil.set(userId.toString(), refreshToken, JwtConstants.REFRESH_EXP_TIME);

        if (username == null) {
            response.sendRedirect(registUrl);
        } else {
            response.sendRedirect(mainUrl);
        }
    }

    public Cookie createCookie(
            String name, String value, int maxAge, boolean httpOnly, boolean secure, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}
