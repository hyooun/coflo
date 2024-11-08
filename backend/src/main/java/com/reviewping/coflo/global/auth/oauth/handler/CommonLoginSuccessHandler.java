package com.reviewping.coflo.global.auth.oauth.handler;

import com.reviewping.coflo.domain.badge.service.BadgeEventService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.service.LoginHistoryService;
import com.reviewping.coflo.domain.user.service.UserService;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class CommonLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final RedisUtil redisUtil;
    private final CookieUtil cookieUtil;
    private final LoginHistoryService loginHistoryService;
    private final BadgeEventService badgeEventService;
    private final UserService userService;
    private final UserProjectRepository userProjectRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        User user = principal.getUser();
        Long userId = user.getId();
        String username = user.getUsername();

        log.info("=== 로그인 성공 ===");
        Map<String, Object> responseMap = Map.of("userId", userId);
        String accessToken = JwtProvider.generateToken(responseMap, JwtConstants.ACCESS_EXP_TIME);
        String refreshToken = JwtProvider.generateToken(responseMap, JwtConstants.REFRESH_EXP_TIME);

        Cookie accessTokenCookie =
                cookieUtil.createCookie(
                        JwtConstants.ACCESS_NAME, accessToken, JwtConstants.ACCESS_EXP_TIME * 60);
        Cookie refreshTokenCookie =
                cookieUtil.createCookie(
                        JwtConstants.REFRESH_NAME,
                        refreshToken,
                        JwtConstants.REFRESH_EXP_TIME * 60);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        redisUtil.set(userId.toString(), refreshToken, JwtConstants.REFRESH_EXP_TIME);
        loginHistoryService.recordLogin(user);
        badgeEventService.eventRandom(user);

        boolean isSignUp = username == null ? true : false;
        boolean isConnect = userService.isConnect(userId);
        Long projectId = userService.getRecentVisitProjectId(user);

        if (isConnect && projectId == null) {
            UserProject userProject = userProjectRepository.findTopByUserIdOrderByCreatedDateDesc(userId);
            projectId = userProject.getId();
        }

        String redirectUrl = cookieUtil.getCookieValue(request, "redirect_url");
        redirectUrl = String.format("%s?isSignup=%b&isConnect=%b&projectId=%s",
            redirectUrl, isSignUp, isConnect, projectId != null ? projectId : "");

        cookieUtil.deleteCookie(request, response, "redirect_url");

        log.info("redirect_url={}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
