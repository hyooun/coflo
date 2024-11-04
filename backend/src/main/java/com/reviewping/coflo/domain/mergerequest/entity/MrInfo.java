package com.reviewping.coflo.domain.mergerequest.entity;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.review.entity.Review;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

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

    @Setter private Integer readabilityScore;
    @Setter private Integer consistencyScore;
    @Setter private Integer reusabilityScore;
    @Setter private Integer reliabilityScore;
    @Setter private Integer securityScore;
    @Setter private Integer maintainabilityScore;

    @Builder
    public MrInfo(Project project, Long gitlabMrIid, LocalDateTime gitlabCreatedDate) {
        this.project = project;
        this.gitlabMrIid = gitlabMrIid;
        this.gitlabCreatedDate = gitlabCreatedDate;
        this.project.getMrInfos().add(this);
    }
}
