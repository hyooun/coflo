package com.reviewping.coflo.domain.userproject.service;

import com.reviewping.coflo.domain.mergerequest.entity.MrInfo;
import com.reviewping.coflo.domain.project.entity.CodeQualityCode;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.CodeQualityCodeRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectScoreRepository;
import com.reviewping.coflo.global.util.ProjectDateUtil;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProjectScoreService {

    private final ProjectDateUtil projectDateUtil;
    private final UserRepository userRepository;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final UserProjectRepository userProjectRepository;
    private final CodeQualityCodeRepository codeQualityCodeRepository;
    private final UserProjectScoreRepository userProjectScoreRepository;

    private Map<Long, CodeQualityCode> codeQualityCodeMap;

    @PostConstruct
    private void init() {
        List<CodeQualityCode> codeQualityCodes = codeQualityCodeRepository.findAll();
        codeQualityCodeMap =
                codeQualityCodes.stream()
                        .collect(Collectors.toMap(CodeQualityCode::getId, code -> code));
    }

    @Transactional
    public void saveUserProjectScores(String username, MrInfo mrInfo) {
        UserProject userProject = getUserProject(username, mrInfo.getProject());
        int currentWeek =
                projectDateUtil.calculateWeekNumber(
                        mrInfo.getProject().getCreatedDate().toLocalDate(), LocalDate.now());

        Map<Long, Integer> scoresMap = initializeScoresMap(mrInfo);
        scoresMap.forEach(
                (codeQualityCodeId, score) ->
                        updateOrSaveUserProjectScore(
                                userProject, currentWeek, codeQualityCodeId, score));
    }

    private UserProject getUserProject(String username, Project project) {
        User user = userRepository.getByUsername(username);
        GitlabAccount gitlabAccount =
                gitlabAccountRepository.getByUserIdAndProjectId(user.getId(), project.getId());
        return userProjectRepository.getByProjectAndGitlabAccount(project, gitlabAccount);
    }

    private Map<Long, Integer> initializeScoresMap(MrInfo mrInfo) {
        Map<Long, Integer> scoresMap = new HashMap<>();
        scoresMap.put(1L, mrInfo.getReadabilityScore());
        scoresMap.put(2L, mrInfo.getConsistencyScore());
        scoresMap.put(3L, mrInfo.getReusabilityScore());
        scoresMap.put(4L, mrInfo.getReliabilityScore());
        scoresMap.put(5L, mrInfo.getSecurityScore());
        scoresMap.put(6L, mrInfo.getMaintainabilityScore());
        return scoresMap;
    }

    private void updateOrSaveUserProjectScore(
            UserProject userProject, int currentWeek, Long codeQualityCodeId, Integer score) {
        Optional<UserProjectScore> optionalScore =
                userProjectScoreRepository.findScoreByProjectWeekAndCode(
                        userProject, currentWeek, codeQualityCodeId);

        if (optionalScore.isPresent()) {
            UserProjectScore existingScore = optionalScore.get();
            existingScore.addToTotalScore(score);
        } else {
            createNewUserProjectScore(userProject, currentWeek, codeQualityCodeId, (long) score);
        }
    }

    private void createNewUserProjectScore(
            UserProject userProject, int currentWeek, Long codeQualityCodeId, long score) {
        UserProjectScore newScore =
                UserProjectScore.builder()
                        .userProject(userProject)
                        .codeQualityCode(codeQualityCodeMap.get(codeQualityCodeId))
                        .week(currentWeek)
                        .totalScore(score)
                        .build();
        userProjectScoreRepository.save(newScore);
    }
}
