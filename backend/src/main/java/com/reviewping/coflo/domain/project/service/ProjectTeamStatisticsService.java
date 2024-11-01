package com.reviewping.coflo.domain.project.service;

import com.reviewping.coflo.domain.badge.entity.BadgeCode;
import com.reviewping.coflo.domain.project.controller.response.LanguageResponse;
import com.reviewping.coflo.domain.project.controller.response.ProjectTeamDetailResponse;
import com.reviewping.coflo.domain.project.controller.response.ProjectTeamRewardResponse;
import com.reviewping.coflo.domain.project.controller.response.UserScoreInfoResponse;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.LanguageCodeRepository;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import com.reviewping.coflo.domain.userproject.repository.UserProjectScoreRepository;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.ProjectInfoContent;
import com.reviewping.coflo.global.util.ProjectDateUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectTeamStatisticsService {

    private final GitLabClient gitLabClient;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final ProjectRepository projectRepository;
    private final UserProjectScoreRepository userProjectScoreRepository;
    private final LanguageCodeRepository languageCodeRepository;
    private final ProjectDateUtil projectDateUtil;

    public ProjectTeamDetailResponse getTeamDetail(User user, Long projectId) {
        GitlabAccount gitlabAccount = gitlabAccountRepository.getFirstByUserId(user.getId());
        Project project = projectRepository.getById(projectId);
        ProjectInfoContent projectInfoContent =
                gitLabClient.getProjectInfoDetail(
                        gitlabAccount.getDomain(),
                        gitlabAccount.getUserToken(),
                        project.getGitlabProjectId());
        Long aiReviewCount = projectRepository.findReviewCountByProjectId(project.getId());
        List<LanguageResponse> languages = createLanguageResponse(projectInfoContent.languages());
        return ProjectTeamDetailResponse.of(projectInfoContent, languages, aiReviewCount);
    }

    public ProjectTeamRewardResponse getTeamScore(User user, Long projectId) {
        Project project = projectRepository.getById(projectId);
        LocalDate projectCreatedDate = project.getCreatedDate().toLocalDate();
        int previousWeek = getPreviousWeek(projectCreatedDate);
        LocalDate[] startAndEndDates =
                projectDateUtil.calculateWeekStartAndEndDates(projectCreatedDate, previousWeek);
        List<UserScoreInfoResponse> userScoreInfoResponses =
                getCurrentUserAndTopUserScore(user, project, previousWeek);
        return ProjectTeamRewardResponse.of(startAndEndDates, userScoreInfoResponses);
    }

    private List<LanguageResponse> createLanguageResponse(Map<String, Double> languages) {
        return languages.entrySet().stream()
                .map(
                        entry -> {
                            String language = entry.getKey();
                            double percentage = entry.getValue();
                            String color =
                                    languageCodeRepository.findColorByNameOrDefault(language);
                            return new LanguageResponse(language, percentage, color);
                        })
                .toList();
    }

    private int getPreviousWeek(LocalDate projectCreatedDate) {
        int currentWeek = projectDateUtil.calculateWeekNumber(projectCreatedDate, LocalDate.now());
        return Math.max(1, currentWeek - 1);
    }

    private List<UserScoreInfoResponse> getCurrentUserAndTopUserScore(
            User user, Project project, int previousWeek) {
        List<UserScoreInfoResponse> totalCurrentUserAndTop5UserScores = new ArrayList<>();
        totalCurrentUserAndTop5UserScores.add(getUserScoreInfo(user, project, previousWeek));
        totalCurrentUserAndTop5UserScores.addAll(getTop5UserScoreInfo(user, project, previousWeek));
        return totalCurrentUserAndTop5UserScores;
    }

    private UserScoreInfoResponse getUserScoreInfo(User user, Project project, int week) {
        List<UserProjectScore> userScores =
                userProjectScoreRepository.findUserProjectScores(
                        user.getId(), project.getId(), week);
        return createUserScoreInfoResponse(user, userScores);
    }

    private List<UserScoreInfoResponse> getTop5UserScoreInfo(
            User user, Project project, int previousWeek) {
        List<UserProjectScore> topScoreUserProjectsWithScores =
                userProjectScoreRepository.findTopUserProjectScores(
                        user.getId(), project.getId(), previousWeek, 5);

        return topScoreUserProjectsWithScores.stream()
                .collect(Collectors.groupingBy(UserProjectScore::getUserProject))
                .entrySet()
                .stream()
                .map(
                        entry -> {
                            User topUser = entry.getKey().getGitlabAccount().getUser();
                            List<UserProjectScore> scores = entry.getValue();
                            return createUserScoreInfoResponse(topUser, scores);
                        })
                .toList();
    }

    private UserScoreInfoResponse createUserScoreInfoResponse(
            User user, List<UserProjectScore> previousWeekScores) {
        BadgeCode badgeCode = user.getMainBadgeCode();
        return UserScoreInfoResponse.of(user, badgeCode, previousWeekScores);
    }
}
