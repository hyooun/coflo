package com.reviewping.coflo.domain.mergerequest.service;

import com.reviewping.coflo.domain.gitlab.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrPageResponse;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrResponse;
import com.reviewping.coflo.domain.mergerequest.entity.MrInfo;
import com.reviewping.coflo.domain.mergerequest.repository.MrInfoRepository;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.GitlabMrDetailContent;
import com.reviewping.coflo.global.client.gitlab.response.GitlabMrPageContent;
import com.reviewping.coflo.global.util.ProjectDateUtil;
import java.time.LocalDate;
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
    private final ProjectRepository projectRepository;
    private final ProjectDateUtil projectDateUtil;

    public GitlabMrPageResponse getGitlabMergeRequests(
            Long userId, Long projectId, String state, GitlabSearchRequest gitlabSearchRequest) {
        GitlabAccount gitlabAccount =
                gitlabAccountRepository.getByUserIdAndProjectId(userId, projectId);
        Project project = projectRepository.getById(projectId);

        GitlabMrPageContent gitlabMrPage =
                gitLabClient.searchGitlabMergeRequests(
                        gitlabAccount.getDomain(),
                        gitlabAccount.getUserToken(),
                        project.getGitlabProjectId(),
                        state,
                        gitlabSearchRequest,
                        gitlabAccount.getCreatedDate());

        List<GitlabMrResponse> gitlabMrResponses = buildGitlabMrResponses(gitlabMrPage);

        return GitlabMrPageResponse.of(gitlabMrResponses, gitlabMrPage.pageDetail());
    }

    public List<GitlabMrResponse> getBestMergeRequests(Long userId, Long projectId) {
        GitlabAccount gitlabAccount =
                gitlabAccountRepository.getByUserIdAndProjectId(userId, projectId);
        Project project = projectRepository.getById(projectId);
        LocalDate[] startAndEndDates =
                projectDateUtil.calculateWeekStartAndEndDates(
                        LocalDate.from(project.getCreatedDate()), LocalDate.now());

        List<MrInfo> mrInfoList =
                mrInfoRepository.findTop3MrInfoList(
                        projectId,
                        startAndEndDates[0].atTime(0, 0, 0),
                        startAndEndDates[1].atTime(0, 0, 0));

        return gitLabClient.getTop3MrList(
                gitlabAccount.getDomain(),
                gitlabAccount.getUserToken(),
                project.getGitlabProjectId(),
                mrInfoList);
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
