package com.reviewping.coflo.domain.review.controller;

import com.reviewping.coflo.domain.review.controller.dto.response.RetrievalDetailResponse;
import com.reviewping.coflo.domain.review.controller.dto.response.ReviewResponse;
import com.reviewping.coflo.domain.review.service.ReviewService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ApiResponse<ReviewResponse> getReviewList(
            @AuthUser User user, @RequestParam Long projectId, @RequestParam Long mergeRequestIid) {
        ReviewResponse reviewResponse =
                reviewService.getReviewList(user.getId(), projectId, mergeRequestIid);
        return ApiSuccessResponse.success(reviewResponse);
    }

    @GetMapping("/{reviewId}/retrievals")
    public ApiResponse<List<RetrievalDetailResponse>> getRetrievalDetail(
            @PathVariable Long reviewId) {
        List<RetrievalDetailResponse> retrievalDetailResponse =
                reviewService.getRetrievalDetail(reviewId);
        return ApiSuccessResponse.success(retrievalDetailResponse);
    }
}
