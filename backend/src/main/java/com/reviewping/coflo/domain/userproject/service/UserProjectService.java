package com.reviewping.coflo.domain.userproject.service;

import static com.reviewping.coflo.global.error.ErrorCode.PROJECT_NOT_EXIST;
import static com.reviewping.coflo.global.error.ErrorCode.USER_GITLAB_ACCOUNT_NOT_EXIST;

import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.userproject.controller.dto.response.UserProjectResponse;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProjectService {

    private final UserProjectRepository userProjectRepository;
    private final GitlabAccountRepository gitlabAccountRepository;

    public List<UserProjectResponse> getUserProjects(User user, Long currentProjectId) {
        GitlabAccount gitlabAccount =
                gitlabAccountRepository
                        .findFirstByUserOrderByIdAsc(user)
                        .orElseThrow(() -> new BusinessException(USER_GITLAB_ACCOUNT_NOT_EXIST));
        List<UserProject> userProjects =
                userProjectRepository.getUserProjectsOrderByModifiedDateDesc(gitlabAccount.getId());

        if (currentProjectId == -1) {
            moveCurrentProjectToFront(currentProjectId, userProjects);
        }

        return userProjects.stream().map(UserProjectResponse::of).toList();
    }

    private void moveCurrentProjectToFront(Long currentProjectId, List<UserProject> userProjects) {
        int index = findProjectIndexById(userProjects, currentProjectId);
        UserProject currentProject = userProjects.remove(index);
        userProjects.addFirst(currentProject);
    }

    private int findProjectIndexById(List<UserProject> userProjects, Long projectId) {
        return IntStream.range(0, userProjects.size())
                .filter(i -> userProjects.get(i).getProject().getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(PROJECT_NOT_EXIST));
    }
}
