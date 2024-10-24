package com.reviewping.coflo.domain.openai.dto.response;

import java.util.List;
import lombok.Builder;

/**
 * @see <a href="https://platform.openai.com/docs/api-reference/chat/object">OpenAI Chat Completion Object</a>
 */
@Builder
public record ChatCompletionContent(
        String id,
        String object,
        long created,
        String model,
        List<Choice> choices,
        Usage usage,
        SystemFingerprint system_fingerprint) {
    @Builder
    public record Choice(int index, ChatMessage message, Object logprobs, String finish_reason) {}

    @Builder
    public record Usage(
            int prompt_tokens,
            int completion_tokens,
            int total_tokens,
            PromptTokensDetails prompt_tokens_details,
            CompletionTokensDetails completion_tokens_details) {
        public record PromptTokensDetails(int cached_tokens) {}

        public record CompletionTokensDetails(int reasoning_tokens) {}
    }

    public record SystemFingerprint(String fingerprint) {}
}
