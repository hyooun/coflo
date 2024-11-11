package com.reviewping.coflo.domain.project.service;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.ProjectWebhookContent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectWebhookService {

    @Value("${domain-webhook-url}")
    private String domainWebhookUrl;

    private final GitLabClient gitLabClient;

    public void addGitlabProjectWebhooks(String projectDomain, Project project) {
        String eventWebhookUrl = domainWebhookUrl + "/" + project.getId();
        if (isWebhookAlreadyRegistered(
                eventWebhookUrl,
                projectDomain,
                project.getBotToken(),
                project.getGitlabProjectId())) {
            return;
        }
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

    private boolean isWebhookAlreadyRegistered(
            String eventWebhookUrl, String domain, String botToken, Long gitlabProjectId) {
        List<ProjectWebhookContent> projectWebhooks =
                gitLabClient.getProjectWebhooks(domain, botToken, gitlabProjectId);
        return projectWebhooks.stream().anyMatch(content -> content.url().equals(eventWebhookUrl));
    }
}
