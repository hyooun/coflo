package com.reviewping.coflo.domain.gitlab.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.reviewping.coflo.domain.gitlab.fixture.GitlabEventRequestFixture;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.fixture.ProjectFixture;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.review.service.ReviewService;
import com.reviewping.coflo.global.client.gitlab.request.GitlabEventRequest;
import com.reviewping.coflo.global.error.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GitlabEventHandlerTest {

    @Mock private ReviewService reviewService;

    @Mock private ProjectRepository projectRepository;

    @InjectMocks private GitlabEventHandler gitlabEventHandler;

    @BeforeEach
    void setUp() {
        gitlabEventHandler.initHandlers();
    }

    @Test
    void handleOpenActionShouldInvokeHandleMergeRequestOpen() {
        // given
        Long projectId = 1L;
        GitlabEventRequest openActionRequest = GitlabEventRequestFixture.createOpenActionRequest();
        Project mockProject = ProjectFixture.createMockProject();
        given(projectRepository.getById(1L)).willReturn(mockProject);

        // when
        gitlabEventHandler.handleMergeRequest(projectId, openActionRequest);

        // then
        then(reviewService)
                .should()
                .makeCodeReviewWhenCalledByWebhook(
                        "gitlab.example.com",
                        mockProject.getBotToken(),
                        openActionRequest.project().id(),
                        openActionRequest.objectAttributes().iid(),
                        openActionRequest.objectAttributes().description(),
                        openActionRequest.objectAttributes().targetBranch(),
                        openActionRequest.objectAttributes().createdAt().toLocalDateTime(),
                        projectId);
    }

    @Test
    void handleMergeRequestUnsupportedActionShouldThrowBusinessException() {
        // given
        GitlabEventRequest unsupportedActionRequest =
                GitlabEventRequestFixture.createUnsupportedActionRequest();

        // when & then
        assertThrows(
                BusinessException.class,
                () -> gitlabEventHandler.handleMergeRequest(1L, unsupportedActionRequest));
    }

    @Test
    void handleMergeRequestInvalidGitlabUrlShouldThrowBusinessException() {
        // given
        GitlabEventRequest invalidUrlRequest = GitlabEventRequestFixture.createInvalidUrlRequest();

        // when & then
        assertThrows(
                BusinessException.class,
                () -> gitlabEventHandler.handleMergeRequest(1L, invalidUrlRequest));
    }
}
