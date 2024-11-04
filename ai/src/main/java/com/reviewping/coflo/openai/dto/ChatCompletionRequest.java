package com.reviewping.coflo.openai.dto;

import java.util.List;

/**
 * @see <a href="https://platform.openai.com/docs/api-reference/chat/create">OpenAI Chat Completion Create</a>
 */
public record ChatCompletionRequest(String model, List<ChatMessage> messages) {}
