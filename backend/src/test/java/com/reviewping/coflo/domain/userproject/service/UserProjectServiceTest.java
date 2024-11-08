package com.reviewping.coflo.domain.userproject.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserProjectServiceTest {

    @Mock private UserProjectRepository userProjectRepository;
    @Mock private GitlabAccountRepository gitlabAccountRepository;

    @InjectMocks private UserProjectService userProjectService;

    @Test
    @DisplayName("사용자가 연동된 프로젝트가 있을 때, true를 반환한다.")
    public void testUserHasLinkedProject() {
        // given
        Long userId = 1L;
        GitlabAccount gitlabAccount = mock(GitlabAccount.class);

        given(gitlabAccountRepository.getFirstByUserId(userId)).willReturn(gitlabAccount);
        given(userProjectRepository.existsByGitlabAccountUserId(userId)).willReturn(true);

        // when
        boolean result = userProjectService.hasLinkedProject(userId).hasLinkedProject();

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("사용자가 연동된 프로젝트가 없을 때, false를 반환한다.")
    public void testUserHasNoLinkedProject() {
        // given
        Long userId = 1L;
        GitlabAccount gitlabAccount = mock(GitlabAccount.class);

        given(gitlabAccountRepository.getFirstByUserId(userId)).willReturn(gitlabAccount);
        given(userProjectRepository.existsByGitlabAccountUserId(userId)).willReturn(false);

        // when
        boolean result = userProjectService.hasLinkedProject(userId).hasLinkedProject();

        // then
        assertThat(result).isFalse();
    }
}
