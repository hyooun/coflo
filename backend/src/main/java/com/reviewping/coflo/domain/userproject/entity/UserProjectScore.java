package com.reviewping.coflo.domain.userproject.entity;

import com.reviewping.coflo.domain.project.entity.CodeQualityCode;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProjectScore extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_project_id")
    private UserProject userProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_quality_code_id")
    private CodeQualityCode codeQualityCode;

    private int week;
    private Long totalScore;

    @Builder
    public UserProjectScore(
            UserProject userProject, CodeQualityCode codeQualityCode, int week, Long totalScore) {
        this.userProject = userProject;
        this.codeQualityCode = codeQualityCode;
        this.week = week;
        this.totalScore = totalScore;
        userProject.getUserProjectScores().add(this);
    }

    public void updateTotalScore(Integer score) {
        if (score == null) score = 0;
        this.totalScore = (totalScore + score) / 2;
    }
}
