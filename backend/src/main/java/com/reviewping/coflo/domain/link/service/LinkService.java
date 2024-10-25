package com.reviewping.coflo.domain.link.service;

import static com.reviewping.coflo.global.error.ErrorCode.USER_GITLAB_ACCOUNT_NOT_EXIST;
import static com.reviewping.coflo.global.error.ErrorCode.USER_NOT_EXIST;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabProjectDetailContent;
import com.reviewping.coflo.domain.gitlab.dto.response.GitlabProjectPageContent;
import com.reviewping.coflo.domain.gitlab.service.GitLabApiService;
import com.reviewping.coflo.domain.link.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.domain.link.controller.dto.request.ProjectLinkReqeust;
import com.reviewping.coflo.domain.link.controller.dto.response.GitlabProjectPageResponse;
import com.reviewping.coflo.domain.link.controller.dto.response.GitlabProjectResponse;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkService {

    private final GitLabApiService gitLabApiService;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;

    public GitlabProjectPageResponse getGitlabProjects(
            Long userId, GitlabSearchRequest gitlabSearchRequest) {
        GitlabAccount gitlabAccount = findGitlabAccountByUserId(userId);
        GitlabProjectPageContent gitlabProjectPage =
                gitLabApiService.searchGitlabProjects(
                        gitlabAccount.getDomain(),
                        gitlabAccount.getUserToken(),
                        gitlabSearchRequest);

        List<GitlabProjectResponse> gitlabProjectList =
                gitlabProjectPage.gitlabProjectDetailContents().stream()
                        .map(project -> buildGitlabProjectResponse(project, gitlabAccount.getId()))
                        .toList();

        return GitlabProjectPageResponse.of(gitlabProjectList, gitlabProjectPage.pageDetail());
    }

    public Long linkGitlabProject(
            Long userId, Long gitlabProjectId, ProjectLinkReqeust projectLinkReqeust) {
        GitlabAccount gitlabAccount = findGitlabAccountByUserId(userId);
        Project project =
                findProjectByGitlabProjectId(gitlabProjectId)
                        .orElseGet(
                                () ->
                                        createProject(
                                                gitlabAccount,
                                                gitlabProjectId,
                                                projectLinkReqeust.botToken()));
        UserProject savedProject =
                userProjectRepository.save(
                        UserProject.builder()
                                .project(project)
                                .gitlabAccount(gitlabAccount)
                                .build());
        return savedProject.getId();
    }

    private GitlabAccount findGitlabAccountByUserId(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST))
                .getGitlabAccounts()
                .stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException(USER_GITLAB_ACCOUNT_NOT_EXIST));
    }

    private GitlabProjectResponse buildGitlabProjectResponse(
            GitlabProjectDetailContent content, Long gitlabAccountId) {
        Optional<Project> optionalProject = findProjectByGitlabProjectId(content.id());
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            boolean isLinked = isProjectLinked(gitlabAccountId, project.getId());
            return GitlabProjectResponse.ofLinkable(content, isLinked);
        }
        return GitlabProjectResponse.ofNonLinkable(content);
    }

    private boolean isProjectLinked(Long gitlabAccountId, Long projectId) {
        return userProjectRepository.existsByGitlabAccountIdAndProjectId(
                gitlabAccountId, projectId);
    }

    private Optional<Project> findProjectByGitlabProjectId(Long gitlabProjectId) {
        return projectRepository.findByGitlabProjectId(gitlabProjectId);
    }

    private Project createProject(
            GitlabAccount gitlabAccount, Long gitlabProjectId, String botToken) {
        GitlabProjectDetailContent gitlabProject =
                gitLabApiService.getSingleProject(
                        gitlabAccount.getDomain(), gitlabAccount.getUserToken(), gitlabProjectId);
        Project project =
                Project.builder()
                        .gitlabProjectId(gitlabProjectId)
                        .botToken(botToken)
                        .name(gitlabProject.name())
                        .build();
        return projectRepository.save(project);
    }
}
