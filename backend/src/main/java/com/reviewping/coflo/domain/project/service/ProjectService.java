package com.reviewping.coflo.domain.project.service;

import static com.reviewping.coflo.global.error.ErrorCode.LINK_BOT_TOKEN_NOT_EXIST;

import com.reviewping.coflo.domain.customPrompt.entity.CustomPrompt;
import com.reviewping.coflo.domain.customPrompt.repository.CustomPromptRepository;
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

        CustomPrompt customPrompt = CustomPrompt.builder().project(project).build();
        customPromptRepository.save(customPrompt);
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
}
