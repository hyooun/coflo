package com.reviewping.coflo.domain.userproject.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.reviewping.coflo.domain.project.entity.MrInfo;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.MrInfoRepository;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.enums.Provider;
import com.reviewping.coflo.domain.user.enums.Role;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.global.config.JpaAuditingConfig;
import com.reviewping.coflo.global.config.QueryDSLConfig;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({QueryDSLConfig.class, JpaAuditingConfig.class})
class UserProjectRepositoryTest {

    @Autowired private UserRepository userRepository;

    @Autowired private ProjectRepository projectRepository;

    @Autowired private GitlabAccountRepository gitlabAccountRepository;

    @Autowired private UserProjectRepository userProjectRepository;

    @Autowired private MrInfoRepository mrInfoRepository;

    private User user;

    private List<MrInfo> mrInfos;

    @BeforeEach
    void setUp() {
        user = createUser();
        GitlabAccount gitlabAccount = createGitlabAccount(user);

        mrInfos = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Project project = createProject(gitlabAccount, i);
            for (int j = 1; j <= 2; j++) {
                MrInfo mrInfo = MrInfo.builder().project(project).gitlabMrIid(1L).build();
                MrInfo savedMrInfo = mrInfoRepository.save(mrInfo);
                mrInfos.add(savedMrInfo);
            }
        }
    }

    @Test
    @DisplayName("사용자의 프로젝트를 최신 업데이트된 날짜 기준으로 정렬한다.")
    public void findProjectsByUserIdOrderByModifiedDateDescTest() throws Exception {
        // given
        mrInfos.get(2).updateGitlabCreatedDate(LocalDateTime.now());
        mrInfos.get(1).updateGitlabCreatedDate(LocalDateTime.now());
        mrInfos.get(3).updateGitlabCreatedDate(LocalDateTime.now());
        mrInfos.get(1).updateGitlabCreatedDate(LocalDateTime.now());
        mrInfos.get(3).updateGitlabCreatedDate(LocalDateTime.now());
        mrInfos.get(2).updateGitlabCreatedDate(LocalDateTime.now());

        // when
        List<UserProject> projects =
                userProjectRepository.getUserProjectsOrderByModifiedDateDesc(user.getId());

        // then
        assertThat(projects).hasSize(3);
        assertThat(projects.get(0).getProject().getName()).isEqualTo("project2");
        assertThat(projects.get(1).getProject().getName()).isEqualTo("project1");
        assertThat(projects.get(2).getProject().getName()).isEqualTo("project3");
    }

    private User createUser() {
        User user =
                User.builder()
                        .username("testUser")
                        .oauth2Id("id")
                        .provider(Provider.KAKAO)
                        .role(Role.USER)
                        .build();
        return userRepository.save(user);
    }

    private GitlabAccount createGitlabAccount(User user) {
        GitlabAccount gitlabAccount =
                GitlabAccount.builder().user(user).userToken("userToken").domain("domain").build();
        return gitlabAccountRepository.save(gitlabAccount);
    }

    private Project createProject(GitlabAccount gitlabAccount, int index) {
        Project project =
                projectRepository.save(
                        Project.builder()
                                .name("project" + index)
                                .botToken("token" + index)
                                .gitlabProjectId((long) index)
                                .build());
        userProjectRepository.save(
                UserProject.builder().gitlabAccount(gitlabAccount).project(project).build());
        return project;
    }
}
