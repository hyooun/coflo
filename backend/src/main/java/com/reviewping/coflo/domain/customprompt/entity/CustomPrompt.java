package com.reviewping.coflo.domain.customprompt.entity;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomPrompt extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private String content = "";

    @Builder
    public CustomPrompt(Project project) {
        this.project = project;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
