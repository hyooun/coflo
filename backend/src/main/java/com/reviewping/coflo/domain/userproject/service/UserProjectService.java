package com.reviewping.coflo.domain.userproject.service;

import static com.reviewping.coflo.global.error.ErrorCode.LINK_BOT_TOKEN_NOT_EXIST;
import static com.reviewping.coflo.global.error.ErrorCode.PROJECT_NOT_EXIST;

import com.reviewping.coflo.domain.gitlab.service.GitLabApiService;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.userproject.controller.dto.request.ProjectLinkRequest;
import com.reviewping.coflo.domain.userproject.controller.dto.response.UserProjectResponse;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProjectService {

    private final GitLabApiService gitLabApiService;
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final GitlabAccountRepository gitlabAccountRepository;

    @Transactional
    public Long linkGitlabProject(
            Long userId, Long gitlabProjectId, ProjectLinkRequest projectLinkRequest) {
        GitlabAccount gitlabAccount = gitlabAccountRepository.getFirstByUserId(userId);
        Project project = getOrCreateProject(gitlabProjectId, projectLinkRequest, gitlabAccount);
        UserProject savedProject =
                userProjectRepository.save(
                        UserProject.builder()
                                .project(project)
                                .gitlabAccount(gitlabAccount)
                                .build());
        return savedProject.getId();
    }

    public boolean hasLinkedProject(Long userId) {
        return userProjectRepository.existsByGitlabAccountUserId(userId);
    }

    public List<UserProjectResponse> getUserProjects(User user, Long currentProjectId) {
        GitlabAccount gitlabAccount = gitlabAccountRepository.getFirstByUserId(user.getId());
        List<UserProject> userProjects =
                userProjectRepository.getUserProjectsOrderByModifiedDateDesc(gitlabAccount.getId());

        if (currentProjectId != -1) {
            moveCurrentProjectToFront(currentProjectId, userProjects);
        }

        return userProjects.stream().map(UserProjectResponse::of).toList();
    }

    private Project getOrCreateProject(
            Long gitlabProjectId,
            ProjectLinkRequest projectLinkRequest,
            GitlabAccount gitlabAccount) {
        return projectRepository
                .findByGitlabProjectId(gitlabProjectId)
                .orElseGet(() -> createProject(gitlabAccount, gitlabProjectId, projectLinkRequest));
    }

    private Project createProject(
            GitlabAccount gitlabAccount,
            Long gitlabProjectId,
            ProjectLinkRequest projectLinkRequest) {

        String gitlabProjectName =
                getProjectNameByBotToken(
                        gitlabAccount.getDomain(), gitlabProjectId, projectLinkRequest);

        return saveProject(gitlabProjectId, projectLinkRequest.botToken(), gitlabProjectName);
    }

    private String getProjectNameByBotToken(
            String domain, Long gitlabProjectId, ProjectLinkRequest projectLinkRequest) {
        if (projectLinkRequest == null || projectLinkRequest.botToken() == null) {
            throw new BusinessException(LINK_BOT_TOKEN_NOT_EXIST);
        }
        return gitLabApiService
                .getSingleProject(domain, projectLinkRequest.botToken(), gitlabProjectId)
                .name();
    }

    private Project saveProject(Long gitlabProjectId, String botToken, String gitlabProjectName) {
        Project project =
                Project.builder()
                        .gitlabProjectId(gitlabProjectId)
                        .botToken(botToken)
                        .name(gitlabProjectName)
                        .build();
        return projectRepository.save(project);
    }

    private void moveCurrentProjectToFront(Long currentProjectId, List<UserProject> userProjects) {
        int index = findProjectIndexById(userProjects, currentProjectId);
        UserProject currentProject = userProjects.remove(index);
        userProjects.addFirst(currentProject);
    }

    private int findProjectIndexById(List<UserProject> userProjects, Long projectId) {
        return IntStream.range(0, userProjects.size())
                .filter(i -> userProjects.get(i).getProject().getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(PROJECT_NOT_EXIST));
    }
}
