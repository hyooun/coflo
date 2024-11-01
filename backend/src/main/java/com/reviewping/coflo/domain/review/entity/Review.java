package com.reviewping.coflo.domain.review.entity;

import com.reviewping.coflo.domain.mergerequest.entity.MrInfo;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mr_info_id")
    private MrInfo mrInfo;

    @OneToMany(mappedBy = "review")
    private List<Retrieval> retrievals = new ArrayList<>();

    @Column(nullable = false)
    private String content;

    @Builder
    public Review(MrInfo mrInfo, String content) {
        this.mrInfo = mrInfo;
        this.content = content;
    }
}
