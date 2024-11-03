package com.reviewping.coflo.domain.review.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.domain.customPrompt.entity.CustomPrompt;
import com.reviewping.coflo.domain.customPrompt.repository.CustomPromptRepository;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrResponse;
import com.reviewping.coflo.domain.mergerequest.entity.MrInfo;
import com.reviewping.coflo.domain.mergerequest.repository.MrInfoRepository;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.review.controller.dto.response.RetrievalDetailResponse;
import com.reviewping.coflo.domain.review.controller.dto.response.ReviewDetailResponse;
import com.reviewping.coflo.domain.review.controller.dto.response.ReviewResponse;
import com.reviewping.coflo.domain.review.entity.Review;
import com.reviewping.coflo.domain.review.message.MrContent;
import com.reviewping.coflo.domain.review.message.ReviewRequest;
import com.reviewping.coflo.domain.review.repository.ReviewRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
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

    private final MrInfoRepository mrInfoRepository;
    private final ReviewRepository reviewRepository;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final CustomPromptRepository customPromptRepository;
    private final ProjectRepository projectRepository;

    private final GitLabClient gitLabClient;
    private final ObjectMapper objectMapper;
    private final RedisGateway redisGateway;

    @Transactional
    @ServiceActivator(inputChannel = "reviewResponseChannel")
    public void handleReviewResponse(String reviewResponseMessage) {
        ReviewResponse reviewResponse;
        try {
            reviewResponse = objectMapper.readValue(reviewResponseMessage, ReviewResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        MrInfo mrInfo = mrInfoRepository.getById(reviewResponse.mrInfoId());
        Project project = mrInfo.getProject();
        Review review = Review.builder().mrInfo(mrInfo).content(reviewResponse.content()).build();
        reviewRepository.save(review);
        // TODO: 참고자료 저장
        gitLabClient.addNoteToMr(
                reviewResponse.gitlabUrl(),
                project.getBotToken(),
                project.getGitlabProjectId(),
                mrInfo.getGitlabMrIid(),
                reviewResponse.content());
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
        // 1. MrInfo 저장
        Project project = projectRepository.getReferenceById(projectId);
        MrInfo mrInfo = mrInfoRepository.save(new MrInfo(project, iid, gitlabCreatedDate));
        // 2. 변경사항가져오기
        List<GitlabMrDiffsContent> mrDiffs =
                gitLabClient.getMrDiffs(gitlabUrl, token, gitlabProjectId, iid);
        // 3. 커스텀프롬프트 가져오기
        CustomPrompt customPrompt = customPromptRepository.getByProjectId(projectId);
        // 3. 리뷰 생성 요청
        MrContent mrContent = new MrContent(mrDescription, mrDiffs.toString());
        ReviewRequest reviewRequest =
                new ReviewRequest(
                        projectId,
                        mrInfo.getId(),
                        targetBranch,
                        mrContent,
                        customPrompt.getContent(),
                        gitlabUrl);
        redisGateway.sendReviewRequest(reviewRequest);
        // 4. TODO: 리뷰 평가 요청
    }

    @Transactional
    public ReviewResponse getReviewList(Long userId, Long projectId, Long mergeRequestIid) {
        MrInfo mrInfo = mrInfoRepository.getByProjectIdAndGitlabMrIid(projectId, mergeRequestIid);
        GitlabAccount gitlabAccount =
                gitlabAccountRepository.getByUserIdAndProjectId(userId, projectId);
        Project project = projectRepository.getById(projectId);
        GitlabMrResponse gitlabMrResponse =
                gitLabClient.getSingleMergeRequest(
                        gitlabAccount.getDomain(),
                        gitlabAccount.getUserToken(),
                        project.getGitlabProjectId(),
                        mergeRequestIid);
        List<ReviewDetailResponse> reviews =
                mrInfo.getReviews().stream().map(ReviewDetailResponse::of).toList();
        return ReviewResponse.of(gitlabMrResponse, reviews);
    }

    public List<RetrievalDetailResponse> getRetrievalDetail(Long reviewId) {
        Review review = reviewRepository.getById(reviewId);
        return review.getRetrievals().stream().map(RetrievalDetailResponse::of).toList();
    }
}
