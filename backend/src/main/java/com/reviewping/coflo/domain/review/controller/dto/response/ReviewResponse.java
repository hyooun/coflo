package com.reviewping.coflo.domain.review.controller.dto.response;

import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrResponse;
import java.util.List;

public record ReviewResponse(GitlabMrResponse mergeRequest, List<ReviewDetailResponse> reviews) {
    public static ReviewResponse of(
            GitlabMrResponse mergeRequest, List<ReviewDetailResponse> reviews) {
        return new ReviewResponse(mergeRequest, reviews);
    }
}
