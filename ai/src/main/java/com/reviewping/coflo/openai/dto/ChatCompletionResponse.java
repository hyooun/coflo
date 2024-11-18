package com.reviewping.coflo.openai.dto;

import java.util.List;

/**
 * @see <a href="https://platform.openai.com/docs/api-reference/chat/object">OpenAI Chat Completion Object</a>
 */
public record ChatCompletionResponse(
        String id, String object, long created, String model, List<Choice> choices, Usage usage) {
    public record Choice(int index, ChatMessage message, Object logprobs, String finish_reason) {}

    public record Usage(
            int prompt_tokens,
            int completion_tokens,
            int total_tokens,
            PromptTokensDetails prompt_tokens_details,
            CompletionTokensDetails completion_tokens_details) {
        public record PromptTokensDetails(int cached_tokens) {}

        public record CompletionTokensDetails(int reasoning_tokens) {}
    }
}
