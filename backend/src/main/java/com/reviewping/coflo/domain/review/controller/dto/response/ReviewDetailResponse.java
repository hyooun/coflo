package com.reviewping.coflo.domain.review.controller.dto.response;

import com.reviewping.coflo.domain.review.entity.Review;
import java.time.LocalDateTime;

public record ReviewDetailResponse(
        Long id, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
    public static ReviewDetailResponse from(Review review) {
        return new ReviewDetailResponse(
                review.getId(),
                review.getContent(),
                review.getCreatedDate(),
                review.getModifiedDate());
    }
}
