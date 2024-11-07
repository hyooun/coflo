package com.reviewping.coflo.service.dto.response;

import com.reviewping.coflo.service.dto.ChunkedCode;

public record RetrievalMessage(String content, String fileName, String language) {
    public static RetrievalMessage from(ChunkedCode chunkedCode) {
        return new RetrievalMessage(
                chunkedCode.getContent(), chunkedCode.getFileName(), chunkedCode.getLanguage());
    }
}
