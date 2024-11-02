package com.reviewping.coflo.domain.project.service;

import com.reviewping.coflo.domain.project.controller.response.CodeQualityScoreResponse;
import com.reviewping.coflo.domain.project.controller.response.ScoreOfWeekResponse;
import com.reviewping.coflo.domain.project.controller.response.UserProjectIndividualScoreResponse;
import com.reviewping.coflo.domain.project.controller.response.UserProjectTotalScoreResponse;
import com.reviewping.coflo.domain.project.domain.mapper.IndividualMapper;
import com.reviewping.coflo.domain.project.domain.mapper.ScoreMapper;
import com.reviewping.coflo.domain.project.domain.mapper.TotalMapper;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectScoreRepository;
import com.reviewping.coflo.global.util.ProjectDateUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectUserStatisticsService {

    private final GitlabAccountRepository gitlabAccountRepository;
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final UserProjectScoreRepository userProjectScoreRepository;
    private final ProjectDateUtil projectDateUtil;

    public UserProjectTotalScoreResponse getTotalCumulativeScore(
            User user, Long projectId, Integer period) {
        UserProject userProject = getUserProject(user, projectId);
        int currentWeek = getCurrentProjectWeek(userProject.getProject());
        int startWeek = Math.max(1, currentWeek - period);
        int endWeek = currentWeek - 1;
        LocalDate[] dateRange =
                projectDateUtil.calculateDateRange(
                        userProject.getProject().getCreatedDate().toLocalDate(),
                        startWeek,
                        endWeek);

        List<ScoreOfWeekResponse> scoreOfWeek =
                getCumulativeScoreOfWeekResponses(startWeek, endWeek, userProject.getId());

        return new UserProjectTotalScoreResponse(dateRange[0], dateRange[1], scoreOfWeek);
    }

    public UserProjectIndividualScoreResponse getIndividualCumulativeScore(
            User user, Long projectId, Integer period) {
        UserProject userProject = getUserProject(user, projectId);
        int currentWeek = getCurrentProjectWeek(userProject.getProject());
        int startWeek = Math.max(1, currentWeek - period);
        int endWeek = currentWeek - 1;
        LocalDate[] dateRange =
                projectDateUtil.calculateDateRange(
                        userProject.getProject().getCreatedDate().toLocalDate(),
                        startWeek,
                        endWeek);

        List<CodeQualityScoreResponse> codeQualityScores =
                getCumulativeCodeQualityScoreResponse(startWeek, endWeek, userProject.getId());

        return new UserProjectIndividualScoreResponse(
                dateRange[0], dateRange[1], codeQualityScores);
    }

    public UserProjectTotalScoreResponse getTotalAcquisitionScore(
            User user, Long projectId, Integer period) {
        UserProject userProject = getUserProject(user, projectId);
        int currentWeek = getCurrentProjectWeek(userProject.getProject());
        int startWeek = Math.max(1, currentWeek - period);
        int endWeek = currentWeek - 1;

        LocalDate[] dateRange =
                projectDateUtil.calculateDateRange(
                        userProject.getProject().getCreatedDate().toLocalDate(),
                        startWeek,
                        endWeek);

        List<UserProjectScore> userProjectScores =
                userProjectScoreRepository.findByUserProjectIdAndWeekRange(
                        userProject.getId(), startWeek, endWeek);

        List<ScoreOfWeekResponse> scoreOfWeek =
                processStatistics(userProjectScores, new TotalMapper());

        return new UserProjectTotalScoreResponse(dateRange[0], dateRange[1], scoreOfWeek);
    }

    public UserProjectIndividualScoreResponse getIndividualAcquisitionScore(
            User user, Long projectId, Integer period) {
        UserProject userProject = getUserProject(user, projectId);
        int currentWeek = getCurrentProjectWeek(userProject.getProject());
        int startWeek = Math.max(1, currentWeek - period);
        int endWeek = currentWeek - 1;
        LocalDate[] dateRange =
                projectDateUtil.calculateDateRange(
                        userProject.getProject().getCreatedDate().toLocalDate(),
                        startWeek,
                        endWeek);

        List<UserProjectScore> userProjectScores =
                userProjectScoreRepository.findByUserProjectIdAndWeekRange(
                        userProject.getId(), startWeek, endWeek);

        List<CodeQualityScoreResponse> codeQualityScores =
                processStatistics(userProjectScores, new IndividualMapper());

        return new UserProjectIndividualScoreResponse(
                dateRange[0], dateRange[1], codeQualityScores);
    }

    private List<ScoreOfWeekResponse> getCumulativeScoreOfWeekResponses(
            int startWeek, int endWeek, Long userProjectId) {
        List<UserProjectScore> userProjectScores =
                userProjectScoreRepository.findByUserProjectIdAndWeekRange(
                        userProjectId, startWeek, endWeek);
        List<ScoreOfWeekResponse> weeklyScores =
                processStatistics(userProjectScores, new TotalMapper());

        List<ScoreOfWeekResponse> cumulativeScores = new ArrayList<>();
        long cumulativeScore = 0;
        for (ScoreOfWeekResponse weeklyScore : weeklyScores) {
            cumulativeScore += weeklyScore.score();
            cumulativeScores.add(new ScoreOfWeekResponse(weeklyScore.week(), cumulativeScore));
        }

        return cumulativeScores;
    }

    private List<CodeQualityScoreResponse> getCumulativeCodeQualityScoreResponse(
            int startWeek, int endWeek, Long userProjectId) {
        List<UserProjectScore> userProjectScores =
                userProjectScoreRepository.findByUserProjectIdAndWeekRange(
                        userProjectId, startWeek, endWeek);
        List<CodeQualityScoreResponse> codeQualityScoreResponses =
                processStatistics(userProjectScores, new IndividualMapper());

        List<CodeQualityScoreResponse> cumulativeCodeQualityScores = new ArrayList<>();
        for (CodeQualityScoreResponse codeQualityScore : codeQualityScoreResponses) {
            List<ScoreOfWeekResponse> cumulativeScores = new ArrayList<>();
            long cumulativeScore = 0;

            for (ScoreOfWeekResponse weeklyScore : codeQualityScore.scoreOfWeek()) {
                cumulativeScore += weeklyScore.score();
                cumulativeScores.add(new ScoreOfWeekResponse(weeklyScore.week(), cumulativeScore));
            }

            cumulativeCodeQualityScores.add(
                    new CodeQualityScoreResponse(
                            codeQualityScore.codeQualityName(), cumulativeScores));
        }

        return cumulativeCodeQualityScores;
    }

    private UserProject getUserProject(User user, Long projectId) {
        GitlabAccount gitlabAccount = gitlabAccountRepository.getFirstByUserId(user.getId());
        Project project = projectRepository.getById(projectId);
        return userProjectRepository.getByProjectAndGitlabAccount(project, gitlabAccount);
    }

    private int getCurrentProjectWeek(Project project) {
        return projectDateUtil.calculateWeekNumber(
                project.getCreatedDate().toLocalDate(), LocalDate.now());
    }

    private <K, T> List<T> processStatistics(
            List<UserProjectScore> userProjectScores, ScoreMapper<K, T> scoreMapper) {
        return userProjectScores.stream().collect(scoreMapper.getCollector()).entrySet().stream()
                .map(scoreMapper.getMapper())
                .toList();
    }
}
