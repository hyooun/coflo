package com.reviewping.coflo.domain.gitlab.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.domain.gitlab.controller.dto.request.GitlabEventRequest;
import com.reviewping.coflo.domain.gitlab.fixture.ChatCompletionContentFixture;
import com.reviewping.coflo.domain.gitlab.fixture.GitlabEventRequestFixture;
import com.reviewping.coflo.domain.gitlab.fixture.GitlabMrDiffsContentFixture;
import com.reviewping.coflo.domain.openai.dto.response.ChatCompletionContent;
import com.reviewping.coflo.domain.openai.service.OpenaiApiService;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.GitlabMrDiffsContent;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.ResponseStatus;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class WebhookControllerTest {

    private WebTestClient webTestClient;

    @LocalServerPort private int port;

    @Autowired private ObjectMapper objectMapper;

    @MockBean private OpenaiApiService openaiApiService;

    @MockBean private GitLabClient gitLabClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        ChatCompletionContent chatCompletionContent = ChatCompletionContentFixture.createMock();
        GitlabMrDiffsContent gitlabMrDiffsContent = GitlabMrDiffsContentFixture.createMock();
        given(openaiApiService.chat(anyString())).willReturn(chatCompletionContent);
        given(gitLabClient.getMrDiffs("gitlab.example.com", "test-token", 1L, 1L))
                .willReturn(List.of(gitlabMrDiffsContent));
    }

    @Test
    @DisplayName("올바른 Gitlab webhokk 요청")
    void givenValidMergeRequestHook_whenHandleGitlabEvent_thenReturnsSuccessResponse()
            throws JsonProcessingException {
        // given
        GitlabEventRequest gitlabEventRequest = GitlabEventRequestFixture.createOpenActionRequest();
        String requestBody = objectMapper.writeValueAsString(gitlabEventRequest);
        Long projectId = 1L;

        // when
        webTestClient
                .post()
                .uri("/webhook/{projectId}", projectId)
                .header("X-Gitlab-Event", "Merge Request Hook")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus()
                .isOk()
                .expectBody(ApiResponse.class)
                .consumeWith(
                        response -> {
                            ApiResponse<?> apiResponse = response.getResponseBody();
                            assertNotNull(apiResponse);
                            assertEquals(apiResponse.getStatus(), ResponseStatus.SUCCESS);
                        });
    }

    @Test
    @DisplayName("request body가 빈 웹훅 요청")
    void givenInvalidEventType_whenHandleGitlabEvent_thenReturnsErrorResponse() {
        // given
        String requestBody = "{}"; // 잘못된 요청으로 빈 JSON을 전송

        // when
        webTestClient
                .post()
                .uri("/webhook/{projectId}", 1L)
                .header("X-Gitlab-Event", "Unsupported Hook")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus()
                .isNotFound()
                .expectBody(ApiResponse.class)
                .consumeWith(
                        response -> {
                            ApiResponse<?> apiResponse = response.getResponseBody();
                            assertNotNull(apiResponse);
                            assertEquals(apiResponse.getStatus(), ResponseStatus.ERROR);
                        });
    }

    @Test
    @DisplayName("Gitlab Mr webhook 형신이 아닌 요청")
    void givenInvalidRequestBody_whenHandleGitlabEvent_thenReturnsSerializationError() {
        // given
        String invalidRequestBody = "{invalid json}"; // 잘못된 JSON 포맷

        // when
        webTestClient
                .post()
                .uri("/webhook/{projectId}", 1L)
                .header("X-Gitlab-Event", "Merge Request Hook")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .bodyValue(invalidRequestBody)
                .exchange()
                // then
                .expectStatus()
                .is5xxServerError()
                .expectBody(ApiResponse.class)
                .consumeWith(
                        response -> {
                            ApiResponse<?> apiResponse = response.getResponseBody();
                            assertNotNull(apiResponse);
                            assertEquals(apiResponse.getStatus(), ResponseStatus.ERROR);
                        });
    }
}
