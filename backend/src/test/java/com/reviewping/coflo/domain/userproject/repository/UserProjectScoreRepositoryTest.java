package com.reviewping.coflo.domain.userproject.repository;

import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import com.reviewping.coflo.global.config.JpaAuditingConfig;
import com.reviewping.coflo.global.config.QueryDSLConfig;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({QueryDSLConfig.class, JpaAuditingConfig.class})
class UserProjectScoreRepositoryTest {

    @Autowired private UserProjectScoreRepository userProjectScoreRepository;

    @Test
    @DisplayName("")
    public void test() throws Exception {
        // given
        Long projectId = 1L;
        Long userId = 1L;
        List<UserProjectScore> topScoresWithProjects =
                userProjectScoreRepository.findTopUserProjectScores(projectId, 2, userId);

        for (UserProjectScore userProjectScore : topScoresWithProjects) {
            System.out.println(
                    userProjectScore.getId()
                            + ", "
                            + userProjectScore.getUserProject().getId()
                            + ","
                            + userProjectScore.getTotalScore());
        }
        // when

        // then
    }
}
