package com.reviewping.coflo.domain.link.service;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabProjectContent;
import com.reviewping.coflo.domain.gitlab.service.GitLabApiService;
import com.reviewping.coflo.domain.link.controller.dto.response.GitlabProjectResponse;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkService {

    private final GitLabApiService gitLabApiService;
    private final UserRepository userRepository;
    private final GitlabAccountRepository gitlabAccountRepository;

    public List<GitlabProjectResponse> getGitlabProjects(Long userId, String keyword) {
        GitlabAccount gitlabAccount = findGitlabAccount(userId);
        List<GitlabProjectContent> searchGitlabProjects =
                gitLabApiService.searchGitlabProjects(
                        gitlabAccount.getDomain(), gitlabAccount.getUserToken(), keyword);
        return searchGitlabProjects.stream().map(GitlabProjectResponse::of).toList();
    }

    private GitlabAccount findGitlabAccount(Long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
        return gitlabAccountRepository
                .findFirstByUserIdOrderByIdAsc(user.getId())
                .orElseThrow(() -> new BusinessException(USER_GITLAB_ACCOUNT_NOT_EXIST));
    }
}
