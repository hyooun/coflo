package com.reviewping.coflo.domain.project.service;

import com.reviewping.coflo.domain.project.controller.response.CodeQualityScoreResponse;
import com.reviewping.coflo.domain.project.controller.response.ScoreOfWeekResponse;
import com.reviewping.coflo.domain.project.controller.response.UserProjectIndividualScoreResponse;
import com.reviewping.coflo.domain.project.controller.response.UserProjectTotalScoreResponse;
import com.reviewping.coflo.domain.project.domain.ProjectWeek;
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
        ProjectWeek projectWeek =
                projectDateUtil.calculateWeekRange(
                        userProject.getProject().getCreatedDate().toLocalDate(),
                        period,
                        LocalDate.now());

        List<ScoreOfWeekResponse> scoreOfWeek =
                getCumulativeScoreOfWeekResponses(
                        projectWeek.startWeek(), projectWeek.endWeek(), userProject.getId());

        return new UserProjectTotalScoreResponse(
                projectWeek.startDate(), projectWeek.endDate(), scoreOfWeek);
    }

    public UserProjectIndividualScoreResponse getIndividualCumulativeScore(
            User user, Long projectId, Integer period) {
        UserProject userProject = getUserProject(user, projectId);
        ProjectWeek projectWeek =
                projectDateUtil.calculateWeekRange(
                        userProject.getProject().getCreatedDate().toLocalDate(),
                        period,
                        LocalDate.now());

        List<CodeQualityScoreResponse> codeQualityScores =
                getCumulativeCodeQualityScoreResponse(
                        projectWeek.startWeek(), projectWeek.endWeek(), userProject.getId());

        return new UserProjectIndividualScoreResponse(
                projectWeek.startDate(), projectWeek.endDate(), codeQualityScores);
    }

    public UserProjectTotalScoreResponse getTotalAcquisitionScore(
            User user, Long projectId, Integer period) {
        UserProject userProject = getUserProject(user, projectId);
        ProjectWeek projectWeek =
                projectDateUtil.calculateWeekRange(
                        userProject.getProject().getCreatedDate().toLocalDate(),
                        period,
                        LocalDate.now());

        List<UserProjectScore> userProjectScores =
                userProjectScoreRepository.findByUserProjectIdAndWeekRange(
                        userProject.getId(), projectWeek.startWeek(), projectWeek.endWeek());

        List<ScoreOfWeekResponse> scoreOfWeek = processMapper(userProjectScores, new TotalMapper());

        return new UserProjectTotalScoreResponse(
                projectWeek.startDate(), projectWeek.endDate(), scoreOfWeek);
    }

    public UserProjectIndividualScoreResponse getIndividualAcquisitionScore(
            User user, Long projectId, Integer period) {
        UserProject userProject = getUserProject(user, projectId);
        ProjectWeek projectWeek =
                projectDateUtil.calculateWeekRange(
                        userProject.getProject().getCreatedDate().toLocalDate(),
                        period,
                        LocalDate.now());

        List<UserProjectScore> userProjectScores =
                userProjectScoreRepository.findByUserProjectIdAndWeekRange(
                        userProject.getId(), projectWeek.startWeek(), projectWeek.endWeek());

        List<CodeQualityScoreResponse> codeQualityScores =
                processMapper(userProjectScores, new IndividualMapper());

        return new UserProjectIndividualScoreResponse(
                projectWeek.startDate(), projectWeek.endDate(), codeQualityScores);
    }

    private List<ScoreOfWeekResponse> getCumulativeScoreOfWeekResponses(
            int startWeek, int endWeek, Long userProjectId) {
        List<UserProjectScore> userProjectScores =
                userProjectScoreRepository.findByUserProjectIdAndWeekRange(
                        userProjectId, startWeek, endWeek);
        List<ScoreOfWeekResponse> weeklyScores =
                processMapper(userProjectScores, new TotalMapper());

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
                processMapper(userProjectScores, new IndividualMapper());

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

    private <K, T> List<T> processMapper(
            List<UserProjectScore> userProjectScores, ScoreMapper<K, T> scoreMapper) {
        return userProjectScores.stream().collect(scoreMapper.getCollector()).entrySet().stream()
                .map(scoreMapper.getMapper())
                .toList();
    }
}
