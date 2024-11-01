package com.reviewping.coflo.domain.userproject.service;

import static com.reviewping.coflo.global.error.ErrorCode.PROJECT_NOT_EXIST;

import com.reviewping.coflo.domain.badge.service.BadgeEventService;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.project.service.ProjectService;
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

    private final ProjectService projectService;
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final BadgeEventService badgeEventService;

    @Transactional
    public Long linkGitlabProject(
            User user, Long gitlabProjectId, ProjectLinkRequest projectLinkRequest) {
        GitlabAccount gitlabAccount = gitlabAccountRepository.getFirstByUserId(user.getId());
        Project project = getOrCreateProject(gitlabProjectId, projectLinkRequest, gitlabAccount);
        UserProject savedProject =
                userProjectRepository.save(
                        UserProject.builder()
                                .project(project)
                                .gitlabAccount(gitlabAccount)
                                .build());

        badgeEventService.eventProjectMaster(user);
        return savedProject.getId();
    }

    @Transactional
    public Long unlinkGitlabProject(User user, Long gitlabProjectId) {
        GitlabAccount gitlabAccount = gitlabAccountRepository.getFirstByUserId(user.getId());
        Project project = projectRepository.getByGitlabProjectId(gitlabProjectId);
        UserProject userProject =
                userProjectRepository.getByProjectAndGitlabAccount(project, gitlabAccount);
        userProjectRepository.delete(userProject);
        return userProject.getId();
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
                .orElseGet(
                        () ->
                                projectService.addProject(
                                        gitlabAccount, gitlabProjectId, projectLinkRequest));
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
