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
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectScoreRepository;
import com.reviewping.coflo.global.client.gitlab.GitLabClient;
import com.reviewping.coflo.global.client.gitlab.response.ProjectInfoContent;
import com.reviewping.coflo.global.util.ProjectDateUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
    private final ProjectDateUtil projectDateUtil;
    private final LanguageCodeRepository languageCodeRepository;
    
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

    public ProjectTeamRewardResponse getTeamScore(Long projectId) {
        Project project = projectRepository.getById(projectId);
        LocalDate projectCreatedDate = project.getCreatedDate().toLocalDate();
        int previousWeek = getPreviousWeek(projectCreatedDate);
        LocalDate[] startAndEndDates =
                projectDateUtil.calculateWeekStartAndEndDates(projectCreatedDate, previousWeek);
        List<UserScoreInfoResponse> userScoreInfoResponses =
                generateUserScoreInfoResponses(project, previousWeek);
        return ProjectTeamRewardResponse.of(startAndEndDates, userScoreInfoResponses);
    }

    private int getPreviousWeek(LocalDate projectCreatedDate) {
        int currentWeek = projectDateUtil.calculateWeekNumber(projectCreatedDate, LocalDate.now());
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
        BadgeCode badgeCode = user.getMainBadgeCode();
        List<UserProjectScore> previousWeekScores =
                userProjectScoreRepository.findByUserProjectAndWeek(userProject, previousWeek);
        return UserScoreInfoResponse.of(user, badgeCode, previousWeekScores);
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
}
