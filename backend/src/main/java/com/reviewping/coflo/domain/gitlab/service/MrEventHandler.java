package com.reviewping.coflo.domain.gitlab.service;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.review.service.ReviewService;
import com.reviewping.coflo.global.client.gitlab.request.GitlabEventRequest;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import jakarta.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MrEventHandler {

    private final Map<String, BiConsumer<Long, GitlabEventRequest>> handlers = new HashMap<>();
    private final ReviewService reviewCreateService;
    private final ProjectRepository projectRepository;

    @PostConstruct
    void initHandlers() {
        handlers.put("open", this::handleOpen);
        handlers.put("reopen", this::handleOpen);
    }

    @Async
    public void handle(Long projectId, GitlabEventRequest gitlabEventRequest) {
        log.debug(
                "#handle:: eventType: {}, action: {}",
                gitlabEventRequest.eventType(),
                gitlabEventRequest.objectAttributes().action());
        log.debug("gitlabEventRequest: {}", gitlabEventRequest);
        handlers.getOrDefault(
                        gitlabEventRequest.objectAttributes().action(),
                        (id, request) -> {
                            throw new BusinessException(ErrorCode.UNSUPPORTED_WEBHOOK_ACTION);
                        })
                .accept(projectId, gitlabEventRequest);
    }

    private void handleOpen(Long projectId, GitlabEventRequest gitlabEventRequest) {
        log.debug("#handleOpen");
        // 1. 필요한 정보 추출
        String gitlabUrl = getGitlabUrl(gitlabEventRequest);
        Long gitlabProjectId = gitlabEventRequest.project().id();
        Long iid = gitlabEventRequest.objectAttributes().iid();
        String mrDescription = gitlabEventRequest.objectAttributes().description();
        String targetBranch = gitlabEventRequest.objectAttributes().targetBranch();
        LocalDateTime gitlabCreatedDate =
                gitlabEventRequest.objectAttributes().createdAt().toLocalDateTime();
        // 2. gitlab project token 찾기
        Project project = projectRepository.getById(projectId);
        String token = project.getBotToken();
        // 3. 리뷰 달기
        reviewCreateService.makeCodeReviewWhenCalledByWebhook(
                gitlabUrl,
                token,
                gitlabProjectId,
                iid,
                mrDescription,
                targetBranch,
                gitlabCreatedDate,
                projectId);
    }

    private String getGitlabUrl(GitlabEventRequest gitlabEventRequest) {
        try {
            URI uri = new URI(gitlabEventRequest.project().webUrl());
            URL parsedUrl = uri.toURL();
            return parsedUrl.getHost();
        } catch (IllegalArgumentException | URISyntaxException | MalformedURLException e) {
            throw new BusinessException(ErrorCode.GITLAB_URL_PARSE_ERROR, e);
        }
    }
}
