package com.reviewping.coflo.domain.userproject.entity;

import com.reviewping.coflo.domain.codequality.entity.CodeQuality;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProjectScore extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_project_id")
    private UserProject userProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_quality_id")
    private CodeQuality codeQuality;

    private Long week;
    private Long totalScore;

    @Builder
    public UserProjectScore(UserProject userProject, CodeQuality codeQuality) {
        this.totalScore = 0L;
        this.userProject = userProject;
        this.codeQuality = codeQuality;
        userProject.getUserProjectScores().add(this);
    }
}
