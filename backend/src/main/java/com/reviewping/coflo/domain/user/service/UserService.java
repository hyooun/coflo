package com.reviewping.coflo.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabUserInfoContent;
import com.reviewping.coflo.domain.gitlab.service.GitLabApiService;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final GitlabAccountRepository gitlabAccountRepository;
	private final GitLabApiService gitLabApiService;

	@Transactional
	public void addGitlabAccount(String domain, String userToken, String oauth2Id) {
		User user =
			userRepository
				.findByOauth2Id(oauth2Id)
				.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST));
		gitlabAccountRepository.save(
			GitlabAccount.builder().user(user).domain(domain).userToken(userToken).build());
	}

	@Transactional
	public void synchronizeUserInfo(String oauth2Id) {
		User user =
			userRepository
				.findByOauth2Id(oauth2Id)
				.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST));
		GitlabUserInfoContent userInfo =
			gitLabApiService.getUserInfo(
				user.getGitlabAccounts().getFirst().getDomain(),
				user.getGitlabAccounts().getFirst().getUserToken());
		user.updateUserInfo(userInfo.username(), userInfo.avatarUrl());
	}
}
