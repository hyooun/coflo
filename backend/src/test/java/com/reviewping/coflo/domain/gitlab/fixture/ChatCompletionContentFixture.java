package com.reviewping.coflo.domain.gitlab.fixture;

import com.reviewping.coflo.domain.openai.dto.response.ChatCompletionContent;
import java.util.List;

public class ChatCompletionContentFixture {
    public static ChatCompletionContent createMock() {
        return new ChatCompletionContent(
                "1",
                null,
                1,
                "",
                List.of(),
                new ChatCompletionContent.Usage(
                        1,
                        1,
                        1,
                        new ChatCompletionContent.Usage.PromptTokensDetails(1),
                        new ChatCompletionContent.Usage.CompletionTokensDetails(1)));
    }
}
