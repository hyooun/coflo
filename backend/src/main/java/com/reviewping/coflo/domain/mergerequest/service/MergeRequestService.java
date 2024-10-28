package com.reviewping.coflo.domain.mergerequest.service;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabMrPageContent;
import com.reviewping.coflo.domain.gitlab.service.GitLabApiService;
import com.reviewping.coflo.domain.link.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.domain.mergerequest.dto.response.GitlabMrPageResponse;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MergeRequestService {

    private final GitLabApiService gitLabApiService;
    private final GitlabAccountRepository gitlabAccountRepository;

    public GitlabMrPageResponse getGitlabMergeRequests(
            Long userId, Long projectId, String state, GitlabSearchRequest gitlabSearchRequest) {
        GitlabAccount gitlabAccount =
                gitlabAccountRepository
                        .findGitlabAccountByUserIdAndProjectId(userId, projectId)
                        .orElseThrow(
                                () ->
                                        new BusinessException(
                                                ErrorCode.USER_GITLAB_ACCOUNT_NOT_EXIST));

        GitlabMrPageContent gitlabMrPage =
                gitLabApiService.searchGitlabMergeRequests(
                        gitlabAccount.getDomain(),
                        gitlabAccount.getUserToken(),
                        projectId,
                        state,
                        gitlabSearchRequest);

        return GitlabMrPageResponse.of(
                gitlabMrPage.gitlabMrDetailContents(), gitlabMrPage.pageDetail());
    }
}
