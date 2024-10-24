package com.reviewping.coflo.global.oauth.handler;

import com.reviewping.coflo.domain.user.entity.PrincipalDetail;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.global.jwt.utils.JwtConstants;
import com.reviewping.coflo.global.jwt.utils.JwtProvider;
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

    private final UserRepository userRepository;
    private final RedisUtil redisUtil;
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
        User user = userRepository.findByOauth2Id(principal.getOauth2Id()).orElse(null);

        log.info("=== 로그인 성공 ===");
        Map<String, Object> responseMap = Map.of("oauthId", principal.getName());
        String accessToken = JwtProvider.generateToken(responseMap, JwtConstants.ACCESS_EXP_TIME);
        String refreshToken = JwtProvider.generateToken(responseMap, JwtConstants.REFRESH_EXP_TIME);

        Cookie accessTokenCookie =
                cookieUtil.createCookie(
                        JwtConstants.ACCESS_NAME,
                        accessToken,
                        (int) JwtConstants.ACCESS_EXP_TIME / 1000);
        Cookie refreshTokenCookie =
                cookieUtil.createCookie(
                        JwtConstants.REFRESH_NAME,
                        refreshToken,
                        (int) JwtConstants.REFRESH_EXP_TIME / 1000);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        redisUtil.set(user.getOauth2Id(), refreshToken, JwtConstants.REFRESH_EXP_TIME);

        if (user.getUsername() == null) {
            response.sendRedirect(registUrl);
        } else {
            response.sendRedirect(mainUrl);
        }
    }
}
