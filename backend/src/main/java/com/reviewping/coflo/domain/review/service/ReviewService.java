package com.reviewping.coflo.domain.review.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.domain.badge.service.BadgeEventService;
import com.reviewping.coflo.domain.customprompt.entity.CustomPrompt;
import com.reviewping.coflo.domain.customprompt.repository.CustomPromptRepository;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrQueryResponse;
import com.reviewping.coflo.domain.mergerequest.entity.MrInfo;
import com.reviewping.coflo.domain.mergerequest.repository.MrInfoRepository;
import com.reviewping.coflo.domain.notification.service.NotificationService;
import com.reviewping.coflo.domain.project.entity.Branch;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.BranchRepository;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.review.controller.dto.request.RegenerateReviewRequest.RetrievalContent;
import com.reviewping.coflo.domain.review.controller.dto.response.RetrievalDetailResponse;
import com.reviewping.coflo.domain.review.controller.dto.response.ReviewDetailResponse;
import com.reviewping.coflo.domain.review.controller.dto.response.ReviewResponse;
import com.reviewping.coflo.domain.review.entity.LanguageType;
import com.reviewping.coflo.domain.review.entity.Retrieval;
import com.reviewping.coflo.domain.review.entity.Review;
import com.reviewping.coflo.domain.review.message.*;
import com.reviewping.coflo.domain.review.message.ReviewRequestMessage.MrContent;
import com.reviewping.coflo.domain.review.message.ReviewResponseMessage;
import com.reviewping.coflo.domain.review.repository.LanguageRepository;
import com.reviewping.coflo.domain.review.repository.RetrievalRepository;
import com.reviewping.coflo.domain.review.repository.ReviewRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.webhookchannel.service.WebhookChannelService;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.GitlabMrDiffsContent;
import com.reviewping.coflo.global.integration.RedisGateway;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final String AI_REVIEW_COMPLETE_MESSAGE = "AI 리뷰가 생성되었습니다.";

    private final BranchRepository branchRepository;
    private final RetrievalRepository retrievalRepository;

    private final MrInfoRepository mrInfoRepository;
    private final ReviewRepository reviewRepository;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final CustomPromptRepository customPromptRepository;
    private final ProjectRepository projectRepository;
    private final BadgeEventService badgeEventService;
    private final WebhookChannelService webhookChannelService;
    private final NotificationService notificationService;

    private final GitLabClient gitLabClient;
    private final ObjectMapper objectMapper;
    private final RedisGateway redisGateway;
    private final LanguageRepository languageRepository;

    @Transactional
    @ServiceActivator(inputChannel = "reviewResponseChannel")
    public void handleReviewResponse(String reviewResponseMessage) {
        ReviewResponseMessage reviewResponse;
        try {
            reviewResponse =
                    objectMapper.readValue(reviewResponseMessage, ReviewResponseMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        MrInfo mrInfo = mrInfoRepository.getById(reviewResponse.mrInfoId());
        Project project = mrInfo.getProject();

        Review review = Review.builder().mrInfo(mrInfo).content(reviewResponse.content()).build();
        Review savedReview = reviewRepository.save(review);
        log.debug("리뷰가 저장되었습니다. Saved Review Id: {}", savedReview.getId());
        int savedRetrievalCount = saveRetrievals(reviewResponse, review);

        gitLabClient.addNoteToMr(
                reviewResponse.gitlabUrl(),
                project.getBotToken(),
                project.getGitlabProjectId(),
                mrInfo.getGitlabMrIid(),
                reviewResponse.content());
        log.debug(
                "Gitlab에 리뷰를 달았습니다. Saved Review Id: {}, Saved Retrieval Count: {}",
                savedReview.getId(),
                savedRetrievalCount);

        notificationService.create(reviewResponse.userId(), AI_REVIEW_COMPLETE_MESSAGE);

        if (!project.getWebhookChannels().isEmpty()) {
            webhookChannelService.sendData(project.getId(), AI_REVIEW_COMPLETE_MESSAGE);
        }
    }

    @Transactional
    @ServiceActivator(inputChannel = "mrEvalResponseChannel")
    public void handleEvalResponse(String mrEvalResponseMessage) {
        MrEvalResponseMessage evalResponse;
        try {
            evalResponse =
                    objectMapper.readValue(mrEvalResponseMessage, MrEvalResponseMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        MrInfo mrInfo = mrInfoRepository.getById(evalResponse.mrInfoId());
        mrInfo.setConsistencyScore(evalResponse.mrEvaluationMessage().consistencyScore());
        mrInfo.setReadabilityScore(evalResponse.mrEvaluationMessage().readabilityScore());
        mrInfo.setSecurityScore(evalResponse.mrEvaluationMessage().securityScore());
        mrInfo.setReliabilityScore(evalResponse.mrEvaluationMessage().reliabilityScore());
        mrInfo.setMaintainabilityScore(evalResponse.mrEvaluationMessage().maintainabilityScore());
        mrInfo.setReusabilityScore(evalResponse.mrEvaluationMessage().reusabilityScore());
    }

    @Transactional
    public void makeCodeReviewWhenCalledByWebhook(
            String gitlabUrl,
            String token,
            Long gitlabProjectId,
            Long iid,
            String mrDescription,
            String targetBranch,
            LocalDateTime gitlabCreatedDate,
            Long projectId) {
        log.debug("#makeCodeReviewWhenCalledByWebhook");
        // 1. MrInfo 저장
        Project project = projectRepository.getReferenceById(projectId);
        MrInfo mrInfo = mrInfoRepository.save(new MrInfo(project, iid, gitlabCreatedDate));
        // 2. 변경사항가져오기
        List<GitlabMrDiffsContent> mrDiffs =
                gitLabClient.getMrDiffs(gitlabUrl, token, gitlabProjectId, iid);
        // 3. 커스텀프롬프트 가져오기
        CustomPrompt customPrompt = customPromptRepository.getByProjectId(projectId);
        // 4. projectId와 targetBranch로 브랜치 id 가져오기
        Branch branch = branchRepository.getByNameAndProject(targetBranch, project);
        // 5. 리뷰 생성 요청
        MrContent mrContent = new MrContent(mrDescription, mrDiffs.toString());
        ReviewRequestMessage reviewRequest =
                new ReviewRequestMessage(
                        projectId,
                        mrInfo.getId(),
                        branch.getId(),
                        mrContent,
                        customPrompt.getContent(),
                        gitlabUrl);
        redisGateway.sendReviewRequest(reviewRequest);
        // 6. 리뷰 평가 요청
        MrEvalRequestMessage evalRequest =
                new MrEvalRequestMessage(mrInfo.getId(), branch.getId(), mrContent);
        redisGateway.sendEvalRequest(evalRequest);
    }

    @Transactional
    public void regenerateReview(
            User user, Long projectId, Long gitlabMrIid, List<RetrievalContent> retrievals) {
        MrInfo mrInfo = mrInfoRepository.getByProjectIdAndGitlabMrIid(projectId, gitlabMrIid);
        Project project = mrInfo.getProject();

        GitlabAccount gitlabAccount =
                gitlabAccountRepository.getByUserIdAndProjectId(user.getId(), project.getId());

        List<GitlabMrDiffsContent> mrDiffs =
                gitLabClient.getMrDiffs(
                        gitlabAccount.getDomain(),
                        gitlabAccount.getUserToken(),
                        project.getGitlabProjectId(),
                        mrInfo.getGitlabMrIid());

        GitlabMrQueryResponse gitlabMrResponse =
                GitlabMrQueryResponse.of(
                        gitLabClient.getSingleMergeRequest(
                                gitlabAccount.getDomain(),
                                gitlabAccount.getUserToken(),
                                project.getFullPath(),
                                mrInfo.getGitlabMrIid()),
                        true);

        CustomPrompt customPrompt = customPromptRepository.getByProjectId(project.getId());
        MrContent mrContent = new MrContent(gitlabMrResponse.description(), mrDiffs.toString());
        Branch branch =
                branchRepository.getByNameAndProject(gitlabMrResponse.targetBranch(), project);

        log.info("send review request userId: {}", user.getId());
        ReviewRegenerateRequestMessage regenerateRequest =
                new ReviewRegenerateRequestMessage(
                        project.getId(),
                        mrInfo.getId(),
                        branch.getId(),
                        user.getId(),
                        mrContent,
                        customPrompt.getContent(),
                        gitlabAccount.getDomain(),
                        retrievals.stream()
                                .map(
                                        retrieval ->
                                                new RetrievalMessage(
                                                        retrieval.content(),
                                                        retrieval.fileName(),
                                                        LanguageType.valueOf(retrieval.language())
                                                                .getType()))
                                .toList());

        badgeEventService.eventFirstAiReviewRegenerate(user);
        redisGateway.sendReviewRegenerateRequest(regenerateRequest);
    }

    @Transactional
    public ReviewResponse getReviewList(Long userId, Long projectId, Long mergeRequestIid) {
        MrInfo mrInfo = mrInfoRepository.getByProjectIdAndGitlabMrIid(projectId, mergeRequestIid);
        GitlabAccount gitlabAccount =
                gitlabAccountRepository.getByUserIdAndProjectId(userId, projectId);
        Project project = projectRepository.getById(projectId);
        GitlabMrQueryResponse gitlabMrQueryContent =
                GitlabMrQueryResponse.of(
                        gitLabClient.getSingleMergeRequest(
                                gitlabAccount.getDomain(),
                                gitlabAccount.getUserToken(),
                                project.getFullPath(),
                                mergeRequestIid),
                        true);
        String gitlabMrDetailUrl = getGitlabMrDetailUrl(gitlabAccount, project, mrInfo);
        List<ReviewDetailResponse> reviews =
                reviewRepository.findByMrInfoOrderByCreatedDateDesc(mrInfo).stream()
                        .map(ReviewDetailResponse::from)
                        .toList();
        return ReviewResponse.of(gitlabMrQueryContent, reviews, gitlabMrDetailUrl);
    }

    public List<RetrievalDetailResponse> getRetrievalDetail(Long reviewId) {
        Review review = reviewRepository.getById(reviewId);
        return review.getRetrievals().stream().map(RetrievalDetailResponse::from).toList();
    }

    private int saveRetrievals(ReviewResponseMessage reviewResponse, Review review) {
        List<Retrieval> retrievals =
                reviewResponse.retrievals().stream()
                        .map(
                                message ->
                                        Retrieval.builder()
                                                .review(review)
                                                .fileName(message.fileName())
                                                .content(message.content())
                                                .language(
                                                        languageRepository.getByType(
                                                                LanguageType.fromType(
                                                                        message.language())))
                                                .build())
                        .toList();
        List<Retrieval> saved = retrievalRepository.saveAll(retrievals);
        log.debug("참고자료가 저장되었습니다. Saved Retrieval Count: {}", saved.size());
        return saved.size();
    }

    private String getGitlabMrDetailUrl(
            GitlabAccount gitlabAccount, Project project, MrInfo mrInfo) {
        return String.format(
                "https://%s/%s/-/merge_requests/%d",
                gitlabAccount.getDomain(), project.getFullPath(), mrInfo.getGitlabMrIid());
    }
}
