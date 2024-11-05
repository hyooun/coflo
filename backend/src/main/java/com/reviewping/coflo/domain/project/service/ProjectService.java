package com.reviewping.coflo.domain.project.service;

import static com.reviewping.coflo.global.error.ErrorCode.LINK_BOT_TOKEN_NOT_EXIST;

import com.reviewping.coflo.domain.customprompt.entity.CustomPrompt;
import com.reviewping.coflo.domain.customprompt.repository.CustomPromptRepository;
import com.reviewping.coflo.domain.project.entity.Branch;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.message.InitRequestMessage;
import com.reviewping.coflo.domain.project.repository.BranchRepository;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.userproject.controller.dto.request.ProjectLinkRequest;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.GitlabProjectDetailContent;
import com.reviewping.coflo.global.error.exception.BusinessException;
import com.reviewping.coflo.global.integration.RedisGateway;
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

    private final RedisGateway redisGateway;
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
        List<Branch> branches = List.of();
        if (projectLinkRequest.branches() != null) {
            branches = saveProjectBranches(projectLinkRequest.branches(), project);
        }
        Project savedProject = projectRepository.save(project);
        saveBasicCustomPrompt(savedProject);
        addGitlabProjectWebhooks(gitlabAccount.getDomain(), savedProject);
        initProject(
                savedProject, branches, projectLinkRequest.botToken(), gitlabAccount.getDomain());
        return savedProject;
    }

    private void initProject(
            Project project, List<Branch> branches, String token, String gitlabDomain) {
        String gitlabUrl = "https://" + gitlabDomain + "/" + project.getName();
        branches.forEach(
                branch -> {
                    InitRequestMessage initRequest =
                            new InitRequestMessage(
                                    project.getId(),
                                    branch.getId(),
                                    gitlabUrl,
                                    branch.getName(),
                                    token);
                    redisGateway.sendInitRequest(initRequest);
                });
    }

    private Project createProject(
            GitlabAccount gitlabAccount,
            Long gitlabProjectId,
            ProjectLinkRequest projectLinkRequest) {
        GitlabProjectDetailContent gitlabProject =
                getProjectContentByBotToken(
                        gitlabAccount.getDomain(), gitlabProjectId, projectLinkRequest);
        return Project.builder()
                .gitlabProjectId(gitlabProjectId)
                .botToken(projectLinkRequest.botToken())
                .name(gitlabProject.name())
                .gitUrl(gitlabProject.httpUrlToRepo())
                .build();
    }

    private GitlabProjectDetailContent getProjectContentByBotToken(
            String domain, Long gitlabProjectId, ProjectLinkRequest projectLinkRequest) {
        if (projectLinkRequest == null || projectLinkRequest.botToken() == null) {
            throw new BusinessException(LINK_BOT_TOKEN_NOT_EXIST);
        }
        return gitLabClient.getSingleProject(
                domain, projectLinkRequest.botToken(), gitlabProjectId);
    }

    private List<Branch> saveProjectBranches(List<String> branchNames, Project project) {
        List<Branch> branches =
                branchNames.stream().map(branchName -> new Branch(project, branchName)).toList();
        return branchRepository.saveAll(branches);
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
