package com.reviewping.coflo.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public void addGitlabAccount(String domain, String userToken, String oauth2Id) {
		User user = userRepository.findByOauth2Id(oauth2Id)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST));
		GitlabAccount gitlabAccount = gitlabAccountRepository.save(GitlabAccount.builder()
			.domain(domain)
			.userToken(userToken)
			.build());

		user.addGitlabAccount(gitlabAccount);
		userRepository.save(user);
	}

}
