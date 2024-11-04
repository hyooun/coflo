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
import com.reviewping.coflo.global.common.repository.BranchRepository;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final BranchRepository branchRepository;

    @Value("${domain-webhook-url}")
    private String domainWebhookUrl;

    private final GitLabClient gitLabClient;
    private final ProjectRepository projectRepository;
    private final CustomPromptRepository customPromptRepository;

    @Transactional
    public Project addProject(
            GitlabAccount gitlabAccount,
            Long gitlabProjectId,
            ProjectLinkRequest projectLinkRequest) {

        Project project = createProject(gitlabAccount, gitlabProjectId, projectLinkRequest);
        if (projectLinkRequest.branches() != null) {
            saveProjectBranches(projectLinkRequest.branches(), project);
        }

        Project savedProject = projectRepository.save(project);
        saveBasicCustomPrompt(savedProject);
        addGitlabProjectWebhooks(gitlabAccount.getDomain(), savedProject);
        return savedProject;
    }

    private Project createProject(
            GitlabAccount gitlabAccount,
            Long gitlabProjectId,
            ProjectLinkRequest projectLinkRequest) {
        String gitlabProjectName =
                getProjectNameByBotToken(
                        gitlabAccount.getDomain(), gitlabProjectId, projectLinkRequest);
        return Project.builder()
                .gitlabProjectId(gitlabProjectId)
                .botToken(projectLinkRequest.botToken())
                .name(gitlabProjectName)
                .build();
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

    private void saveProjectBranches(List<String> branches, Project project) {
        branches.forEach(
                branchName -> {
                    Branch branch = Branch.builder().name(branchName).project(project).build();
                    project.addBranch(branch);
                    branchRepository.save(branch);
                });
    }

    private void saveBasicCustomPrompt(Project project) {
        CustomPrompt customPrompt = CustomPrompt.builder().project(project).build();
        customPromptRepository.save(customPrompt);
    }

    private void addGitlabProjectWebhooks(String projectDomain, Project project) {
        String eventWebhookUrl = domainWebhookUrl + "/" + project.getId();
        Map<String, Boolean> eventSettings = new HashMap<>();
        eventSettings.put("merge_requests_events", true);
        eventSettings.put("push_events", true);
        gitLabClient.addProjectWebhook(
                projectDomain,
                project.getBotToken(),
                project.getGitlabProjectId(),
                eventWebhookUrl,
                eventSettings);
    }
}
