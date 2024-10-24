package com.reviewping.coflo.domain.openai.dto.response;

import java.util.List;

/**
 * @see <a href="https://platform.openai.com/docs/api-reference/chat/create">OpenAI Chat Completion Create</a>
 */
// TODO: model, role을 enum으로
public record ChatCompletionRequest(String model, List<ChatMessage> messages) {}
