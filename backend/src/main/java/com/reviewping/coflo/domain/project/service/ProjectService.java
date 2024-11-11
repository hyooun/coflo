package com.reviewping.coflo.domain.project.service;

import static com.reviewping.coflo.global.error.ErrorCode.LINK_BOT_TOKEN_NOT_EXIST;

import com.reviewping.coflo.domain.customprompt.entity.CustomPrompt;
import com.reviewping.coflo.domain.customprompt.repository.CustomPromptRepository;
import com.reviewping.coflo.domain.project.entity.Branch;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.message.UpdateRequestMessage;
import com.reviewping.coflo.domain.project.repository.BranchRepository;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.userproject.controller.dto.request.ProjectLinkRequest;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.GitlabProjectDetailContent;
import com.reviewping.coflo.global.error.exception.BusinessException;
import com.reviewping.coflo.global.integration.RedisGateway;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final RedisGateway redisGateway;
    private final BranchRepository branchRepository;
    private final GitLabClient gitLabClient;
    private final ProjectRepository projectRepository;
    private final CustomPromptRepository customPromptRepository;
    private final ProjectWebhookService projectWebhookService;

    @Transactional
    public Project addProject(
            GitlabAccount gitlabAccount,
            Long gitlabProjectId,
            ProjectLinkRequest projectLinkRequest) {
        Project project = createAndSaveProject(gitlabAccount, gitlabProjectId, projectLinkRequest);
        List<Branch> branches = saveBranches(projectLinkRequest, project);
        saveBasicCustomPrompt(project);
        setupProjectIntegration(
                project.getId(), branches, projectLinkRequest.botToken(), project.getGitUrl());
        addProjectWebhooks(gitlabAccount, project);
        return project;
    }

    private Project createAndSaveProject(
            GitlabAccount gitlabAccount,
            Long gitlabProjectId,
            ProjectLinkRequest projectLinkRequest) {
        GitlabProjectDetailContent gitlabProject =
                getProjectContentByBotToken(
                        gitlabAccount.getDomain(), gitlabProjectId, projectLinkRequest);
        Project project =
                Project.builder()
                        .gitlabProjectId(gitlabProjectId)
                        .botToken(projectLinkRequest.botToken())
                        .name(gitlabProject.name())
                        .gitUrl(gitlabProject.httpUrlToRepo())
                        .fullPath(gitlabProject.fullPath())
                        .build();
        return projectRepository.save(project);
    }

    private GitlabProjectDetailContent getProjectContentByBotToken(
            String domain, Long gitlabProjectId, ProjectLinkRequest projectLinkRequest) {
        if (projectLinkRequest == null || projectLinkRequest.botToken() == null) {
            throw new BusinessException(LINK_BOT_TOKEN_NOT_EXIST);
        }
        return gitLabClient.getSingleProject(
                domain, projectLinkRequest.botToken(), gitlabProjectId);
    }

    private List<Branch> saveBranches(ProjectLinkRequest projectLinkRequest, Project project) {
        if (projectLinkRequest.branches() == null) return List.of();
        List<Branch> branches =
                projectLinkRequest.branches().stream()
                        .map(branchName -> new Branch(project, branchName))
                        .toList();
        return branchRepository.saveAll(branches);
    }

    private void saveBasicCustomPrompt(Project project) {
        CustomPrompt customPrompt = CustomPrompt.builder().project(project).build();
        customPromptRepository.save(customPrompt);
    }

    private void setupProjectIntegration(
            Long projectId, List<Branch> branches, String token, String gitlabUrl) {
        branches.forEach(
                branch -> {
                    UpdateRequestMessage initRequest =
                            new UpdateRequestMessage(
                                    projectId,
                                    branch.getId(),
                                    gitlabUrl,
                                    branch.getName(),
                                    token,
                                    "");
                    redisGateway.sendUpdateRequest(initRequest);
                });
    }

    private void addProjectWebhooks(GitlabAccount gitlabAccount, Project project) {
        projectWebhookService.addGitlabProjectWebhooks(gitlabAccount.getDomain(), project);
    }
}
