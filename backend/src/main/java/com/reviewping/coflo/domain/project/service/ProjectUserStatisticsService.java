package com.reviewping.coflo.domain.project.service;

import com.reviewping.coflo.domain.codequality.entity.CodeQualityCode;
import com.reviewping.coflo.domain.codequality.repository.CodeQualityCodeRepository;
import com.reviewping.coflo.domain.project.controller.response.CodeQualityScoreResponse;
import com.reviewping.coflo.domain.project.controller.response.ScoreOfWeekResponse;
import com.reviewping.coflo.domain.project.controller.response.UserProjectIndividualScoreResponse;
import com.reviewping.coflo.domain.project.controller.response.UserProjectTotalScoreResponse;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectScoreRepository;
import com.reviewping.coflo.global.util.ProjectDateUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    private final CodeQualityCodeRepository codeQualityCodeRepository;
    private final ProjectDateUtil projectDateUtil;

    public UserProjectTotalScoreResponse getTotalCumulativeScore(
            User user, Long projectId, Integer period) {
        return null;
    }

    public UserProjectIndividualScoreResponse getIndividualCumulativeScore(
            User user, Long projectId, Integer period) {
        return null;
    }

    public UserProjectTotalScoreResponse getTotalAcquisitionScore(
            User user, Long projectId, Integer period) {
        UserProject userProject = getUserProject(user, projectId);
        int currentWeek = getCurrentProjectWeek(userProject.getProject());
        int startWeek = Math.max(1, currentWeek - period);
        int endWeek = currentWeek - 1;

        List<ScoreOfWeekResponse> scoreOfWeek =
                getScoreOfWeekResponses(startWeek, endWeek, userProject);
        LocalDate[] dateRange =
                projectDateUtil.calculateDateRange(
                        userProject.getProject().getCreatedDate().toLocalDate(),
                        startWeek,
                        endWeek);

        return new UserProjectTotalScoreResponse(dateRange[0], dateRange[1], scoreOfWeek);
    }

    public UserProjectIndividualScoreResponse getIndividualAcquisitionScore(
            User user, Long projectId, Integer period) {
        UserProject userProject = getUserProject(user, projectId);
        int currentWeek = getCurrentProjectWeek(userProject.getProject());
        int startWeek = Math.max(1, currentWeek - period);
        int endWeek = currentWeek - 1;

        List<CodeQualityScoreResponse> codeQualityScores =
                getCodeQualityScoreResponse(startWeek, endWeek, userProject.getId());
        LocalDate[] dateRange =
                projectDateUtil.calculateDateRange(
                        userProject.getProject().getCreatedDate().toLocalDate(),
                        startWeek,
                        endWeek);
        return new UserProjectIndividualScoreResponse(
                dateRange[0], dateRange[1], codeQualityScores);
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

    private List<ScoreOfWeekResponse> getScoreOfWeekResponses(
            int startWeek, int endWeek, UserProject userProject) {
        List<ScoreOfWeekResponse> scoreOfWeek = new ArrayList<>();
        for (int week = startWeek; week <= endWeek; week++) {
            Long totalScoreSum =
                    userProjectScoreRepository.getTotalScoreSumByUserProjectIdAndWeek(
                            userProject.getId(), week);
            scoreOfWeek.add(new ScoreOfWeekResponse(week, totalScoreSum));
        }
        return scoreOfWeek;
    }

    private List<CodeQualityScoreResponse> getCodeQualityScoreResponse(
            int startWeek, int endWeek, Long userProjectId) {
        List<CodeQualityCode> codeQualityCodes = codeQualityCodeRepository.findAll();
        return codeQualityCodes.stream()
                .map(
                        codeQuality ->
                                getCodeQualityScoreResponse(
                                        startWeek, endWeek, userProjectId, codeQuality))
                .collect(Collectors.toList());
    }

    private CodeQualityScoreResponse getCodeQualityScoreResponse(
            int startWeek, int endWeek, Long userProjectId, CodeQualityCode codeQuality) {
        List<ScoreOfWeekResponse> scoresOfWeek =
                userProjectScoreRepository
                        .findByUserProjectIdAndCodeQualityIdAndWeekRange(
                                userProjectId, codeQuality.getId(), startWeek, endWeek)
                        .stream()
                        .map(ScoreOfWeekResponse::of)
                        .toList();
        return new CodeQualityScoreResponse(codeQuality.getName(), scoresOfWeek);
    }
}
