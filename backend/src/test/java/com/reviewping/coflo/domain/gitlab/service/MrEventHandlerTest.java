package com.reviewping.coflo.domain.gitlab.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.reviewping.coflo.domain.gitlab.dto.request.GitlabEventRequest;
import com.reviewping.coflo.domain.gitlab.fixture.GitlabEventRequestFixture;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.fixture.ProjectFixture;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.review.service.ReviewCreateService;
import com.reviewping.coflo.global.error.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MrEventHandlerTest {

    @Mock private ReviewCreateService reviewCreateService;

    @Mock private ProjectRepository projectRepository;

    @InjectMocks private MrEventHandler mrEventHandler;

    @BeforeEach
    void setUp() {
        mrEventHandler.initHandlers();
    }

    @Test
    void handleOpenActionShouldInvokeHandleOpen() {
        // given
        Long projectId = 1L;
        GitlabEventRequest openActionRequest = GitlabEventRequestFixture.createOpenActionRequest();
        Project mockProject = ProjectFixture.createMockProject();
        given(projectRepository.getById(1L)).willReturn(mockProject);

        // when
        mrEventHandler.handle(projectId, openActionRequest);

        // then
        then(reviewCreateService)
                .should()
                .makeCodeReviewWhenCalledByWebhook(
                        "gitlab.example.com",
                        mockProject.getBotToken(),
                        openActionRequest.project().id(),
                        openActionRequest.objectAttributes().iid(),
                        openActionRequest.objectAttributes().description(),
                        projectId);
    }

    @Test
    void handleUnsupportedActionShouldThrowBusinessException() {
        // given
        GitlabEventRequest unsupportedActionRequest =
                GitlabEventRequestFixture.createUnsupportedActionRequest();

        // when & then
        assertThrows(
                BusinessException.class, () -> mrEventHandler.handle(1L, unsupportedActionRequest));
    }

    @Test
    void handleInvalidGitlabUrlShouldThrowBusinessException() {
        // given
        GitlabEventRequest invalidUrlRequest = GitlabEventRequestFixture.createInvalidUrlRequest();

        // when & then
        assertThrows(BusinessException.class, () -> mrEventHandler.handle(1L, invalidUrlRequest));
    }
}
