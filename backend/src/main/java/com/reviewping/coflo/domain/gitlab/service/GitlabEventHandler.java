package com.reviewping.coflo.domain.gitlab.service;

import com.reviewping.coflo.domain.project.entity.Branch;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.message.UpdateRequestMessage;
import com.reviewping.coflo.domain.project.repository.BranchRepository;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.review.service.ReviewService;
import com.reviewping.coflo.global.client.gitlab.request.GitlabEventRequest;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import com.reviewping.coflo.global.integration.RedisGateway;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitlabEventHandler {

    private final ReviewService reviewCreateService;
    private final ProjectRepository projectRepository;
    private final RedisGateway redisGateway;
    private final BranchRepository branchRepository;
    private final Map<String, BiConsumer<Long, GitlabEventRequest>> handlers =
            Map.of(
                    "open",
                    this::handleOpen,
                    "reopen",
                    this::handleOpen,
                    "merge",
                    this::handleMerge);

    @Async
    public void handleMergeRequest(Long projectId, GitlabEventRequest gitlabEventRequest) {
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

    @Transactional
    public void handleMerge(Long projectId, GitlabEventRequest gitlabEventRequest) {
        log.debug("#handleMerged");
        Project project = projectRepository.getById(projectId);
        String branchName = gitlabEventRequest.objectAttributes().targetBranch();
        Branch branch = branchRepository.getByNameAndProject(branchName, project);
        UpdateRequestMessage updateRequest =
                new UpdateRequestMessage(
                        project.getId(),
                        branch.getId(),
                        project.getGitUrl(),
                        branch.getName(),
                        project.getBotToken(),
                        "");
        redisGateway.sendUpdateRequest(updateRequest);
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
