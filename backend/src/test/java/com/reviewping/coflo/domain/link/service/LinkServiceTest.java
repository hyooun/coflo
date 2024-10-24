package com.reviewping.coflo.domain.link.service;

import static com.reviewping.coflo.global.error.ErrorCode.USER_NOT_EXIST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.reviewping.coflo.IntegrationTestSupport;
import com.reviewping.coflo.domain.gitlab.service.GitLabApiService;
import com.reviewping.coflo.domain.link.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.global.error.exception.BusinessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LinkServiceTest extends IntegrationTestSupport {

    @Autowired private LinkService linkService;
    @Autowired private GitLabApiService gitLabApiService;
    @Autowired private UserRepository userRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private UserProjectRepository userProjectRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        projectRepository.deleteAllInBatch();
        userProjectRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("조회하려는 유저가 존재하지 않으면 예외가 발생한다.")
    public void test() throws Exception {
        // given
        Long userId = 1L;
        GitlabSearchRequest gitlabSearchRequest = GitlabSearchRequest.of("", 1, 10);

        // when
        // then
        assertThatThrownBy(() -> linkService.getGitlabProjects(userId, gitlabSearchRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage(USER_NOT_EXIST.getMessage());
    }
}
