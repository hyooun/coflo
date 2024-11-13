package com.reviewping.coflo.domain.mergerequest.service;

import com.reviewping.coflo.domain.mergerequest.controller.dto.request.GitlabMrPageRequest;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrPageResponse;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrQueryResponse;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrResponse;
import com.reviewping.coflo.domain.mergerequest.entity.MrInfo;
import com.reviewping.coflo.domain.mergerequest.repository.MrInfoRepository;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.GitlabMrDetailContent;
import com.reviewping.coflo.global.client.gitlab.response.GitlabMrPageContent;
import com.reviewping.coflo.global.util.ProjectDateUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final UserProjectRepository userProjectRepository;

    public GitlabMrPageResponse getGitlabMergeRequests(
            Long userId, Long projectId, GitlabMrPageRequest request) {
        GitlabAccount gitlabAccount =
                gitlabAccountRepository.getByUserIdAndProjectId(userId, projectId);
        Project project = projectRepository.getById(projectId);
        UserProject userProject =
                userProjectRepository.getByProjectAndGitlabAccount(project, gitlabAccount);

        GitlabMrPageContent gitlabMrPage =
                gitLabClient.searchGitlabMergeRequests(
                        gitlabAccount.getDomain(),
                        gitlabAccount.getUserToken(),
                        project.getGitlabProjectId(),
                        request,
                        userProject.getCreatedDate());

        List<GitlabMrResponse> gitlabMrResponses = buildGitlabMrResponses(gitlabMrPage);

        return GitlabMrPageResponse.of(gitlabMrResponses, gitlabMrPage.pageDetail());
    }

    public List<GitlabMrQueryResponse> getBestMergeRequests(Long userId, Long projectId) {
        GitlabAccount gitlabAccount =
                gitlabAccountRepository.getByUserIdAndProjectId(userId, projectId);
        Project project = projectRepository.getById(projectId);
        List<MrInfo> mrInfoList = getTop3MrInfos(project);

        return gitLabClient.getTop3MrList(
                gitlabAccount.getDomain(),
                gitlabAccount.getUserToken(),
                project.getFullPath(),
                mrInfoList);
    }

    public List<String> getUsernameBestMergeRequests(Project project) {
        Long userId = project.getUserProjects().getFirst().getGitlabAccount().getUser().getId();
        List<GitlabMrQueryResponse> top3MrList = getBestMergeRequests(userId, project.getId());

        return top3MrList.stream().map(top -> top.assignee().username()).toList();
    }

    private List<MrInfo> getTop3MrInfos(Project project) {
        LocalDate[] startAndEndDates =
                projectDateUtil.calculateWeekStartAndEndDates(
                        LocalDate.from(project.getCreatedDate()), LocalDate.now());
        List<MrInfo> mrInfoList =
                mrInfoRepository.findTop3MrInfoList(
                        project.getId(),
                        startAndEndDates[0].atTime(0, 0, 0),
                        startAndEndDates[1].atTime(0, 0, 0));
        return mrInfoList;
    }

    private List<GitlabMrResponse> buildGitlabMrResponses(GitlabMrPageContent gitlabMrPage) {
        return gitlabMrPage.gitlabMrDetailContents().stream()
                .map(this::createGitlabMrResponse)
                .toList();
    }

    private GitlabMrResponse createGitlabMrResponse(GitlabMrDetailContent content) {
        boolean exists = mrInfoRepository.existsByGitlabMrIid(content.iid());
        LocalDateTime lastReviewCreatedAt =
                mrInfoRepository.findLatestReviewDateByGitlabMrIid(content.iid());
        return GitlabMrResponse.of(content, exists, lastReviewCreatedAt);
    }
}
