package com.reviewping.coflo.openai;

import java.util.List;

/**
 * @see <a href="https://platform.openai.com/docs/api-reference/embeddings/create">OpenAI Chat Embedding Response</a>
 */
public record EmbeddingResponse(
        String object, List<EmbeddingObject> data, String model, Usage usage) {
    public record EmbeddingObject(String object, float[] embedding, int index) {}

    public record Usage(int prompt_tokens, int total_tokens) {}
}
