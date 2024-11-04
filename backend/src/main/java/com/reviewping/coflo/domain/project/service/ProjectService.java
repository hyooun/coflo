package com.reviewping.coflo.domain.project.service;

import static com.reviewping.coflo.global.error.ErrorCode.LINK_BOT_TOKEN_NOT_EXIST;

import com.reviewping.coflo.domain.customprompt.entity.CustomPrompt;
import com.reviewping.coflo.domain.customprompt.repository.CustomPromptRepository;
import com.reviewping.coflo.domain.project.entity.Branch;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.userproject.controller.dto.request.ProjectLinkRequest;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final GitLabClient gitLabClient;
    private final ProjectRepository projectRepository;
    private final CustomPromptRepository customPromptRepository;

    @Transactional
    public Project addProject(
            GitlabAccount gitlabAccount,
            Long gitlabProjectId,
            ProjectLinkRequest projectLinkRequest) {

        String gitlabProjectName =
                getProjectNameByBotToken(
                        gitlabAccount.getDomain(), gitlabProjectId, projectLinkRequest);

        Project project =
                Project.builder()
                        .gitlabProjectId(gitlabProjectId)
                        .botToken(projectLinkRequest.botToken())
                        .name(gitlabProjectName)
                        .build();

        saveProjectBranches(projectLinkRequest, project);
        saveBasicCustomPrompt(project);
        return projectRepository.save(project);
    }

    private String getProjectNameByBotToken(
            String domain, Long gitlabProjectId, ProjectLinkRequest projectLinkRequest) {
        if (projectLinkRequest == null || projectLinkRequest.botToken() == null) {
            throw new BusinessException(LINK_BOT_TOKEN_NOT_EXIST);
        }
        return gitLabClient
                .getSingleProject(domain, projectLinkRequest.botToken(), gitlabProjectId)
                .name();
    }

    private void saveBasicCustomPrompt(Project project) {
        CustomPrompt customPrompt = CustomPrompt.builder().project(project).build();
        customPromptRepository.save(customPrompt);
    }

    private static void saveProjectBranches(
            ProjectLinkRequest projectLinkRequest, Project project) {
        projectLinkRequest
                .branches()
                .forEach(
                        branchName -> {
                            Branch branch =
                                    Branch.builder().name(branchName).project(project).build();
                            project.addBranch(branch);
                        });
    }
}
