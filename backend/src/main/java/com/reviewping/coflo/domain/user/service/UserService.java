package com.reviewping.coflo.domain.user.service;

import com.reviewping.coflo.domain.badge.service.BadgeEventService;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.global.auth.jwt.utils.JwtConstants;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.GitlabUserInfoContent;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import com.reviewping.coflo.global.util.CookieUtil;
import com.reviewping.coflo.global.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProjectRepository userProjectRepository;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final GitLabClient gitLabClient;
    private final BadgeEventService badgeEventService;

    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    @Transactional
    public void addGitlabAccount(String domain, String userToken, Long userId) {
        User user = userRepository.getById(userId);
        badgeEventService.eventFirstLogin(user);
        gitLabClient.getUserInfo(domain, userToken);

        try {
            gitlabAccountRepository.save(
                    GitlabAccount.builder().user(user).domain(domain).userToken(userToken).build());
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.USER_TOKEN_ALREADY_EXIST);
        }

        if (user.getUsername() == null) synchronizeUserInfo(userId);
    }

    @Transactional
    public void synchronizeUserInfo(Long userId) {
        User user = userRepository.getById(userId);
        GitlabUserInfoContent userInfo =
                gitLabClient.getUserInfo(
                        user.getGitlabAccounts().getFirst().getDomain(),
                        user.getGitlabAccounts().getFirst().getUserToken());
        user.updateUserInfo(userInfo.username(), userInfo.avatarUrl());
    }

    @Transactional
    public void updateRecentProjectId(Long userId, Long projectId) {
        GitlabAccount gitlabAccount = gitlabAccountRepository.getFirstByUserId(userId);
        gitlabAccount.updateRecentProjectId(projectId);
    }

    public boolean isConnect(Long userId) {
        User user = userRepository.getById(userId);
        List<GitlabAccount> gitlabAccounts = gitlabAccountRepository.findAllByUser(user);
        List<Long> gitlabAccountIds =
                gitlabAccounts.stream().map(gitlabAccount -> gitlabAccount.getId()).toList();
        Long connectCount = userProjectRepository.countByGitlabAccountIds(gitlabAccountIds);

        return connectCount > 0 ? true : false;
    }

    public Long getRecentVisitProjectId(User user) {
        List<GitlabAccount> gitlabAccounts = gitlabAccountRepository.findAllByUser(user);
        return gitlabAccounts.isEmpty() ? null : gitlabAccounts.getFirst().getVisitedProjectId();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response, Long userId) {
        cookieUtil.deleteCookie(request, response, JwtConstants.ACCESS_NAME);
        cookieUtil.deleteCookie(request, response, JwtConstants.REFRESH_NAME);
        redisUtil.delete(userId.toString());
    }
}
