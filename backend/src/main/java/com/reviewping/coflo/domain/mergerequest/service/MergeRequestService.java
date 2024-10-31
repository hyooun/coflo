package com.reviewping.coflo.domain.mergerequest.service;

import com.reviewping.coflo.domain.gitlab.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrPageResponse;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrResponse;
import com.reviewping.coflo.domain.project.repository.MrInfoRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.GitlabMrDetailContent;
import com.reviewping.coflo.global.client.gitlab.response.GitlabMrPageContent;
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
                gitlabAccountRepository.getByUserIdAndProjectId(userId, projectId);

        GitlabMrPageContent gitlabMrPage =
                gitLabClient.searchGitlabMergeRequests(
                        gitlabAccount.getDomain(),
                        gitlabAccount.getUserToken(),
                        projectId,
                        state,
                        gitlabSearchRequest,
                        gitlabAccount.getCreatedDate());

        List<GitlabMrResponse> gitlabMrResponses = buildGitlabMrResponses(gitlabMrPage);

        return GitlabMrPageResponse.of(gitlabMrResponses, gitlabMrPage.pageDetail());
    }

    private List<GitlabMrResponse> buildGitlabMrResponses(GitlabMrPageContent gitlabMrPage) {
        return gitlabMrPage.gitlabMrDetailContents().stream()
                .map(this::createGitlabMrResponse)
                .toList();
    }

    private GitlabMrResponse createGitlabMrResponse(GitlabMrDetailContent content) {
        boolean exists = mrInfoRepository.existsByGitlabMrIid(content.iid());
        return GitlabMrResponse.of(content, exists);
    }
}
