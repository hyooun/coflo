package com.reviewping.coflo.domain.mergerequest.service;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabMrPageContent;
import com.reviewping.coflo.domain.gitlab.service.GitLabClient;
import com.reviewping.coflo.domain.link.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrPageResponse;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrResponse;
import com.reviewping.coflo.domain.project.repository.MrInfoRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MergeRequestService {

    private final GitLabClient gitLabClient;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final MrInfoRepository mrInfoRepository;

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
                gitLabClient.searchGitlabMergeRequests(
                        gitlabAccount.getDomain(),
                        gitlabAccount.getUserToken(),
                        projectId,
                        state,
                        gitlabSearchRequest);

        List<GitlabMrResponse> gitlabMrResponseList =
                gitlabMrPage.gitlabMrDetailContents().stream()
                        .map(
                                content ->
                                        GitlabMrResponse.of(
                                                content,
                                                mrInfoRepository.existsByGitlabMrIid(
                                                        content.iid())))
                        .toList();

        return GitlabMrPageResponse.of(gitlabMrResponseList, gitlabMrPage.pageDetail());
    }
}
