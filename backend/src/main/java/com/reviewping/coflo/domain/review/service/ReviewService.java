package com.reviewping.coflo.domain.review.service;

import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrResponse;
import com.reviewping.coflo.domain.mergerequest.entity.MrInfo;
import com.reviewping.coflo.domain.mergerequest.repository.MrInfoRepository;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.review.controller.dto.response.RetrievalDetailResponse;
import com.reviewping.coflo.domain.review.controller.dto.response.ReviewDetailResponse;
import com.reviewping.coflo.domain.review.controller.dto.response.ReviewResponse;
import com.reviewping.coflo.domain.review.entity.Review;
import com.reviewping.coflo.domain.review.repository.ReviewRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final MrInfoRepository mrInfoRepository;
    private final ReviewRepository reviewRepository;
    private final GitLabClient gitLabClient;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void saveReview(Long projectId, Long iid, String chatResult) {
        MrInfo mrInfo = mrInfoRepository.getByProjectIdAndGitlabMrIid(projectId, iid);
        Review review = Review.builder().mrInfo(mrInfo).content(chatResult).build();
        reviewRepository.save(review);
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
