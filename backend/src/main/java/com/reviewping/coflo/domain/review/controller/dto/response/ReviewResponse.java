package com.reviewping.coflo.domain.review.controller.dto.response;

import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrQueryResponse;
import java.util.List;

public record ReviewResponse(
        GitlabMrQueryResponse mergeRequest,
        List<ReviewDetailResponse> reviews,
        String gitlabMrDetailUrl) {
    public static ReviewResponse of(
            GitlabMrQueryResponse mergeRequest,
            List<ReviewDetailResponse> reviews,
            String gitlabMrDetailUrl) {
        return new ReviewResponse(mergeRequest, reviews, gitlabMrDetailUrl);
    }
}
