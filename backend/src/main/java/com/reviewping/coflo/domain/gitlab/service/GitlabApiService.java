package com.reviewping.coflo.domain.gitlab.service;

import com.reviewping.coflo.domain.gitlab.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.domain.gitlab.controller.dto.response.GitlabProjectPageResponse;
import com.reviewping.coflo.domain.gitlab.controller.dto.response.GitlabProjectResponse;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.GitlabProjectDetailContent;
import com.reviewping.coflo.global.client.gitlab.response.GitlabProjectPageContent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GitlabApiService {

    private final GitLabClient gitLabClient;
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final GitlabAccountRepository gitlabAccountRepository;

    public GitlabProjectPageResponse getGitlabProjects(
            Long userId, GitlabSearchRequest gitlabSearchRequest) {
        GitlabAccount gitlabAccount = gitlabAccountRepository.getFirstByUserId(userId);
        GitlabProjectPageContent gitlabProjectPage =
                gitLabClient.searchGitlabProjects(
                        gitlabAccount.getDomain(),
                        gitlabAccount.getUserToken(),
                        gitlabSearchRequest);

        List<GitlabProjectResponse> gitlabProjectList =
                gitlabProjectPage.gitlabProjectDetailContents().stream()
                        .map(project -> buildGitlabProjectResponse(project, gitlabAccount.getId()))
                        .toList();

        return GitlabProjectPageResponse.of(gitlabProjectList, gitlabProjectPage.pageDetail());
    }

    private GitlabProjectResponse buildGitlabProjectResponse(
            GitlabProjectDetailContent content, Long gitlabAccountId) {
        return projectRepository
                .findByGitlabProjectId(content.id())
                .map(
                        project -> {
                            boolean isLinked = isProjectLinked(gitlabAccountId, project.getId());
                            return GitlabProjectResponse.ofLinkable(content, isLinked);
                        })
                .orElseGet(() -> GitlabProjectResponse.ofNonLinkable(content));
    }

    private boolean isProjectLinked(Long gitlabAccountId, Long projectId) {
        return userProjectRepository.existsByGitlabAccountIdAndProjectId(
                gitlabAccountId, projectId);
    }
}
