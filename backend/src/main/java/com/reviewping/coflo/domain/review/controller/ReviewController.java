package com.reviewping.coflo.domain.review.controller;

import com.reviewping.coflo.domain.review.controller.dto.request.RegenerateReviewRequest;
import com.reviewping.coflo.domain.review.controller.dto.response.RetrievalDetailResponse;
import com.reviewping.coflo.domain.review.controller.dto.response.ReviewResponse;
import com.reviewping.coflo.domain.review.service.ReviewService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.aop.LogExecution;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@LogExecution
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    @Operation(summary = "리뷰 리스트 조회", description = "MR에 대한 AI 작성 리뷰 리스트 조회")
    public ApiResponse<ReviewResponse> getReviewList(
            @AuthUser User user, @RequestParam Long projectId, @RequestParam Long mergeRequestIid) {
        ReviewResponse reviewResponse =
                reviewService.getReviewList(user.getId(), projectId, mergeRequestIid);
        return ApiSuccessResponse.success(reviewResponse);
    }

    @PostMapping
    @Operation(summary = "리뷰 재생성 요청", description = "참고 자료 수정/삭제 후 요청")
    public ApiResponse<Void> regenerateReview(
            @AuthUser User user, @RequestBody RegenerateReviewRequest request) {
        reviewService.regenerateReview(user, request.mrInfoId(), request.retrievals());
        return ApiSuccessResponse.success();
    }

    @GetMapping("/{reviewId}/retrievals")
    @Operation(summary = "참고 자료 상세 조회", description = "리뷰에 사용된 참고 자료 조회")
    public ApiResponse<List<RetrievalDetailResponse>> getRetrievalDetail(
            @PathVariable Long reviewId) {
        List<RetrievalDetailResponse> retrievalDetailResponse =
                reviewService.getRetrievalDetail(reviewId);
        return ApiSuccessResponse.success(retrievalDetailResponse);
    }
}
