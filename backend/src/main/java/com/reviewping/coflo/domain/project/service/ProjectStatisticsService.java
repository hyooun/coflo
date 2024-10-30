package com.reviewping.coflo.domain.project.service;

import com.reviewping.coflo.domain.badge.entity.Badge;
import com.reviewping.coflo.domain.badge.entity.UserBadge;
import com.reviewping.coflo.domain.badge.repository.UserBadgeRepository;
import com.reviewping.coflo.domain.project.controller.response.ProjectTeamDetailResponse;
import com.reviewping.coflo.domain.project.controller.response.ProjectTeamRewardResponse;
import com.reviewping.coflo.domain.project.controller.response.UserScoreInfoResponse;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectScoreRepository;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.ProjectInfoContent;
import com.reviewping.coflo.global.util.ProjectDateUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectStatisticsService {

    private final GitLabClient gitLabClient;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final UserProjectScoreRepository userProjectScoreRepository;
    private final UserBadgeRepository userBadgeRepository;

    public ProjectTeamDetailResponse getTeamDetail(User user, Long projectId) {
        GitlabAccount gitlabAccount = gitlabAccountRepository.getFirstByUserId(user.getId());
        Project project = projectRepository.getById(projectId);
        ProjectInfoContent projectInfoContent =
                gitLabClient.getProjectInfoDetail(
                        gitlabAccount.getDomain(),
                        gitlabAccount.getUserToken(),
                        project.getGitlabProjectId());
        Long aiReviewCount = projectRepository.findReviewCountByProjectId(project.getId());
        return ProjectTeamDetailResponse.of(projectInfoContent, aiReviewCount);
    }

    public ProjectTeamRewardResponse getTeamScore(Long projectId) {
        Project project = projectRepository.getById(projectId);
        LocalDate projectCreatedDate = project.getCreatedDate().toLocalDate();
        int previousWeek = getPreviousWeek(projectCreatedDate);
        LocalDate[] startAndEndDates =
                ProjectDateUtil.calculateWeekStartAndEndDates(projectCreatedDate, previousWeek);
        List<UserScoreInfoResponse> userScoreInfoResponses =
                generateUserScoreInfoResponses(project, previousWeek);
        return ProjectTeamRewardResponse.of(startAndEndDates, userScoreInfoResponses);
    }

    private int getPreviousWeek(LocalDate projectCreatedDate) {
        int currentWeek = ProjectDateUtil.calculateWeekNumber(projectCreatedDate, LocalDate.now());
        return Math.max(1, currentWeek - 1);
    }

    private List<UserScoreInfoResponse> generateUserScoreInfoResponses(
            Project project, int previousWeek) {
        return userProjectRepository.findByProject(project).stream()
                .map(userProject -> createUserScoreInfoResponse(userProject, previousWeek))
                .toList();
    }

    private UserScoreInfoResponse createUserScoreInfoResponse(
            UserProject userProject, int previousWeek) {
        User user = userProject.getGitlabAccount().getUser();
        Badge badge = getUserRepresentativeBadge(user);
        List<UserProjectScore> scoreForPreviousWeek =
                userProjectScoreRepository.findByUserProjectAndWeek(userProject, previousWeek);
        return UserScoreInfoResponse.of(user, badge, scoreForPreviousWeek);
    }

    private Badge getUserRepresentativeBadge(User user) {
        Optional<UserBadge> optionalUserBadge = userBadgeRepository.findSelectedBadgeByUser(user);
        return optionalUserBadge.map(UserBadge::getBadge).orElse(null);
    }
}
