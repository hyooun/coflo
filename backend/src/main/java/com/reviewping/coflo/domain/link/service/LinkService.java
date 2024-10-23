package com.reviewping.coflo.domain.link.service;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabProjectDetailContent;
import com.reviewping.coflo.domain.gitlab.dto.response.GitlabProjectPageContent;
import com.reviewping.coflo.domain.gitlab.service.GitLabApiService;
import com.reviewping.coflo.domain.link.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.domain.link.controller.dto.response.GitlabProjectPageResponse;
import com.reviewping.coflo.domain.link.controller.dto.response.GitlabProjectResponse;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.reviewping.coflo.global.error.ErrorCode.USER_GITLAB_ACCOUNT_NOT_EXIST;
import static com.reviewping.coflo.global.error.ErrorCode.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkService {

    private final GitLabApiService gitLabApiService;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;

    public GitlabProjectPageResponse getGitlabProjects(
            Long userId, GitlabSearchRequest gitlabSearchRequest) {
        GitlabAccount gitlabAccount = findGitlabAccountByUserId(userId);
        GitlabProjectPageContent gitlabProjectPage =
                gitLabApiService.searchGitlabProjects(
                        gitlabAccount.getDomain(),
                        gitlabAccount.getUserToken(),
                        gitlabSearchRequest);

        List<GitlabProjectResponse> gitlabProjectList =
                gitlabProjectPage.gitlabProjectDetailContents().stream()
                        .map(project -> buildGitlabProjectResponse(project, gitlabAccount.getId()))
                        .toList();

        return GitlabProjectPageResponse.of(gitlabProjectList, gitlabProjectPage.pageDetail());
    }

    private GitlabAccount findGitlabAccountByUserId(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST))
                .getGitlabAccounts()
                .stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException(USER_GITLAB_ACCOUNT_NOT_EXIST));
    }

    private GitlabProjectResponse buildGitlabProjectResponse(
            GitlabProjectDetailContent content, Long gitlabAccountId) {
        Optional<Project> optionalProject = projectRepository.findByGitlabProjectId(content.id());
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            boolean isLinked =
                    userProjectRepository.existsByGitlabAccountIdAndProjectId(
                            gitlabAccountId, project.getId());
            return GitlabProjectResponse.of(content, true, isLinked);
        }
        return GitlabProjectResponse.of(content, false, false);
    }
}
