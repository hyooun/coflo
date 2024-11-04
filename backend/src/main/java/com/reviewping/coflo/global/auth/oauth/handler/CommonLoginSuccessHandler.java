package com.reviewping.coflo.global.auth.oauth.handler;

import com.reviewping.coflo.domain.badge.service.BadgeEventService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.service.LoginHistoryService;
import com.reviewping.coflo.global.auth.jwt.utils.JwtConstants;
import com.reviewping.coflo.global.auth.jwt.utils.JwtProvider;
import com.reviewping.coflo.global.auth.oauth.model.UserDetails;
import com.reviewping.coflo.global.util.CookieUtil;
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
    private final CookieUtil cookieUtil;
    private final LoginHistoryService loginHistoryService;
    private final BadgeEventService badgeEventService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        User user = principal.getUser();
        Long userId = user.getId();

        log.info("=== 로그인 성공 ===");
        Map<String, Object> responseMap = Map.of("userId", userId);
        String accessToken = JwtProvider.generateToken(responseMap, JwtConstants.ACCESS_EXP_TIME);
        String refreshToken = JwtProvider.generateToken(responseMap, JwtConstants.REFRESH_EXP_TIME);

        Cookie accessTokenCookie =
                cookieUtil.createCookie(
                        JwtConstants.ACCESS_NAME,
                        accessToken,
                        (int) JwtConstants.ACCESS_EXP_TIME * 60);
        Cookie refreshTokenCookie =
                cookieUtil.createCookie(
                        JwtConstants.REFRESH_NAME,
                        refreshToken,
                        (int) JwtConstants.REFRESH_EXP_TIME * 60);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        redisUtil.set(userId.toString(), refreshToken, JwtConstants.REFRESH_EXP_TIME);
        loginHistoryService.recordLogin(user);
        badgeEventService.eventRandomLucky(user);

        if (principal.getName().equals("empty")) {
            response.sendRedirect(registUrl);
        } else {
            response.sendRedirect(mainUrl);
        }
    }
}
