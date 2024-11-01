package com.reviewping.coflo.domain.userproject.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
class UserProjectScoreRepositoryCustomImplTest {

    @Autowired private UserProjectScoreRepositoryCustomImpl userProjectScoreRepositoryCustomImpl;
    static final int CodeQualitySize = 6;

    @Test
    @DisplayName("현재 유저를 제외한 상위 Top N의 프로젝트 스코어 점수를 조회한다.")
    public void testGetTopUserProjectScores() throws Exception {
        // given
        Long userId = 1L;
        Long projectId = 1L;
        int week = 2;
        int limit = 2;

        // when
        List<UserProjectScore> topUserProjectScores =
                userProjectScoreRepositoryCustomImpl.findTopUserProjectScores(
                        userId, projectId, week, limit);

        // then
        assertThat(topUserProjectScores)
                .isNotEmpty()
                .hasSize(limit * CodeQualitySize)
                .extracting(score -> score.getUserProject().getId())
                .containsOnly(4L, 2L);

        topUserProjectScores.forEach(
                score -> {
                    assertEquals(week, score.getWeek());
                });
    }
}
