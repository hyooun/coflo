package com.reviewping.coflo.domain.review.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabMrDiffsContent;
import com.reviewping.coflo.domain.gitlab.service.GitLabClient;
import com.reviewping.coflo.domain.openai.dto.response.ChatCompletionContent;
import com.reviewping.coflo.domain.openai.dto.response.ChatMessage;
import com.reviewping.coflo.domain.openai.service.OpenaiApiService;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReviewCreateServiceTest {

    @Mock private GitLabClient gitLabClient;
    @Mock private OpenaiApiService openAIApiService;
    @Mock private ReviewService reviewService;

    @InjectMocks private ReviewCreateService reviewCreateService;

    @Test
    public void shouldCreateReviewSuccessfully() {
        // given
        String gitlabUrl = "https://gitlab.com";
        String token = "token";
        Long gitlabProjectId = 1L;
        Long iid = 100L;
        String mrDescription = "This is an MR description";
        Long projectId = 1L;

        // GitlabMrDiffsContent mock data
        GitlabMrDiffsContent mrDiffs =
                new GitlabMrDiffsContent("old/path", "new/path", "diff content");

        // ChatCompletionContent mock data
        String chatResult = "This is a review suggestion";
        ChatMessage chatCompletionMessage = new ChatMessage("assistant", chatResult);
        ChatCompletionContent.Choice chatCompletionChoice =
                ChatCompletionContent.Choice.builder().message(chatCompletionMessage).build();
        List<ChatCompletionContent.Choice> chatCompletionChoices = List.of(chatCompletionChoice);
        ChatCompletionContent chatCompletionContent =
                ChatCompletionContent.builder().choices(chatCompletionChoices).build();

        given(gitLabClient.getMrDiffs(gitlabUrl, token, gitlabProjectId, iid))
                .willReturn(List.of(mrDiffs));
        given(openAIApiService.chat(anyString())).willReturn(chatCompletionContent);

        // when
        reviewCreateService.makeCodeReviewWhenCalledByWebhook(
                gitlabUrl, token, gitlabProjectId, iid, mrDescription, projectId);

        // then
        then(gitLabClient).should().getMrDiffs(gitlabUrl, token, gitlabProjectId, iid);
        then(openAIApiService).should().chat(anyString());
        then(reviewService).should().saveReview(projectId, iid, chatResult);
        then(gitLabClient).should().addNoteToMr(gitlabUrl, token, gitlabProjectId, iid, chatResult);
    }

    @Test
    public void shouldThrowExceptionWhenGitLabApiFails() {
        // given
        String gitlabUrl = "https://gitlab.com";
        String token = "token";
        Long gitlabProjectId = 1L;
        Long iid = 100L;
        String mrDescription = "This is an MR description";
        Long projectId = 1L;

        given(gitLabClient.getMrDiffs(gitlabUrl, token, gitlabProjectId, iid))
                .willThrow(new BusinessException(ErrorCode.EXTERNAL_API_BAD_REQUEST));

        // when & then
        assertThrows(
                BusinessException.class,
                () ->
                        reviewCreateService.makeCodeReviewWhenCalledByWebhook(
                                gitlabUrl, token, gitlabProjectId, iid, mrDescription, projectId));

        then(openAIApiService).should(never()).chat(anyString());
        then(reviewService).should(never()).saveReview(anyLong(), anyLong(), anyString());
        then(gitLabClient)
                .should(never())
                .addNoteToMr(anyString(), anyString(), anyLong(), anyLong(), anyString());
    }
}
