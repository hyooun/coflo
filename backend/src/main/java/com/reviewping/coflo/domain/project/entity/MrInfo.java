package com.reviewping.coflo.domain.project.entity;

import com.reviewping.coflo.domain.review.entity.Review;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MrInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "mrInfo")
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(nullable = false)
    private Long gitlabMrIid;

    private LocalDateTime gitlabCreatedDate;

    // TODO: skillN_score 이름정하기

    @Builder
    public MrInfo(Project project, Long gitlabMrIid, LocalDateTime gitlabCreatedDate) {
        this.project = project;
        this.gitlabMrIid = gitlabMrIid;
        this.gitlabCreatedDate = gitlabCreatedDate;

        this.project.getMrInfos().add(this);
    }
}
