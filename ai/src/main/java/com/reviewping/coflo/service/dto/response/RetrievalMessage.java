package com.reviewping.coflo.service.dto.response;

import com.reviewping.coflo.entity.ChunkedCode;

public record RetrievalMessage(String content, String fileName) {
    public static RetrievalMessage from(ChunkedCode chunkedCode) {
        return new RetrievalMessage(chunkedCode.getContent(), chunkedCode.getFileName());
    }
}
