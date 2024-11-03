package com.reviewping.coflo.domain.project.service;

import com.reviewping.coflo.domain.project.controller.response.UserProjectIndividualScoreResponse;
import com.reviewping.coflo.domain.project.controller.response.UserProjectTotalScoreResponse;
import com.reviewping.coflo.domain.project.domain.ProjectWeek;
import com.reviewping.coflo.domain.project.domain.ScoreType;
import com.reviewping.coflo.domain.project.domain.calculator.IndividualScoreCalculator;
import com.reviewping.coflo.domain.project.domain.calculator.TotalScoreCalculator;
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

    public UserProjectTotalScoreResponse calculateTotalScore(
            User user, Long projectId, Integer period, ScoreType scoreType) {
        UserProject userProject = getUserProject(user, projectId);
        ProjectWeek projectWeek =
                projectDateUtil.calculateWeekRange(
                        userProject.getProject().getCreatedDate().toLocalDate(),
                        period,
                        LocalDate.now());

        List<UserProjectScore> userProjectScores =
                userProjectScoreRepository.findByUserProjectIdAndWeekRange(
                        userProject.getId(), projectWeek.startWeek(), projectWeek.endWeek());

        TotalScoreCalculator calculator = new TotalScoreCalculator(scoreType);
        return calculator.calculateScore(projectWeek, userProjectScores);
    }

    public UserProjectIndividualScoreResponse calculateIndividualScore(
            User user, Long projectId, Integer period, ScoreType scoreType) {
        UserProject userProject = getUserProject(user, projectId);
        ProjectWeek projectWeek =
                projectDateUtil.calculateWeekRange(
                        userProject.getProject().getCreatedDate().toLocalDate(),
                        period,
                        LocalDate.now());

        List<UserProjectScore> userProjectScores =
                userProjectScoreRepository.findByUserProjectIdAndWeekRange(
                        userProject.getId(), projectWeek.startWeek(), projectWeek.endWeek());

        IndividualScoreCalculator calculator = new IndividualScoreCalculator(scoreType);
        return calculator.calculateScore(projectWeek, userProjectScores);
    }

    private UserProject getUserProject(User user, Long projectId) {
        GitlabAccount gitlabAccount = gitlabAccountRepository.getFirstByUserId(user.getId());
        Project project = projectRepository.getById(projectId);
        return userProjectRepository.getByProjectAndGitlabAccount(project, gitlabAccount);
    }
}
