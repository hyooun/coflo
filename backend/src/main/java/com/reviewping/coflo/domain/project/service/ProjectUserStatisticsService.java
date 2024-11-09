package com.reviewping.coflo.domain.project.service;

import com.reviewping.coflo.domain.project.domain.CalculationType;
import com.reviewping.coflo.domain.project.domain.ProjectWeek;
import com.reviewping.coflo.domain.project.domain.ScoreDisplayType;
import com.reviewping.coflo.domain.project.domain.calculator.ScoreCalculator;
import com.reviewping.coflo.domain.project.domain.calculator.ScoreCalculatorFactory;
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
    private final ScoreCalculatorFactory scoreCalculatorFactory;

    public <R> R calculateScore(
            User user,
            Long projectId,
            Integer period,
            CalculationType calculationType,
            ScoreDisplayType scoreDisplayType) {

        UserProject userProject = getUserProject(user, projectId);
        ProjectWeek projectWeek = getProjectWeek(period, userProject);

        List<UserProjectScore> userProjectScores =
                userProjectScoreRepository.findByUserProjectIdAndWeekRange(
                        userProject.getId(), projectWeek.startWeek(), projectWeek.endWeek());

        ScoreCalculator<?, ?, R> scoreCalculator =
                scoreCalculatorFactory.get(calculationType, scoreDisplayType);
        return scoreCalculator.process(projectWeek, userProjectScores);
    }

    private ProjectWeek getProjectWeek(Integer period, UserProject userProject) {
        return projectDateUtil.calculateWeekRange(
                userProject.getProject().getCreatedDate().toLocalDate(), period, LocalDate.now());
    }

    private UserProject getUserProject(User user, Long projectId) {
        GitlabAccount gitlabAccount = gitlabAccountRepository.getFirstByUserId(user.getId());
        Project project = projectRepository.getById(projectId);
        return userProjectRepository.getByProjectAndGitlabAccount(project, gitlabAccount);
    }
}
