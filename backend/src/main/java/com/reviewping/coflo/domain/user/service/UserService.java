package com.reviewping.coflo.domain.user.service;

import com.reviewping.coflo.domain.badge.service.BadgeEventService;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.GitlabUserInfoContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final GitLabClient gitLabClient;
    private final BadgeEventService badgeEventService;

    @Transactional
    public void addGitlabAccount(String domain, String userToken, Long userId) {
        User user = userRepository.getById(userId);
        checkFirstLogin(userId, user);

        gitlabAccountRepository.save(
                GitlabAccount.builder().user(user).domain(domain).userToken(userToken).build());
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

    private void checkFirstLogin(Long userId, User user) {
        if (user.getGitlabAccounts().size() <= 0) {
            badgeEventService.addFirstLogin(userId);
        }
    }
}
