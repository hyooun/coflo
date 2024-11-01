package com.reviewping.coflo.domain.review.controller.dto.response;

import com.reviewping.coflo.domain.review.entity.LanguageType;
import com.reviewping.coflo.domain.review.entity.Retrieval;

public record RetrievalDetailResponse(
        Long id, String fileName, String content, LanguageType language) {
    public static RetrievalDetailResponse of(Retrieval retrieval) {
        return new RetrievalDetailResponse(
                retrieval.getId(),
                retrieval.getFileName(),
                retrieval.getContent(),
                retrieval.getLanguage().getType());
    }
}
