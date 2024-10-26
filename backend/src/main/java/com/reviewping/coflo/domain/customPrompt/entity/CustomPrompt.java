package com.reviewping.coflo.domain.customPrompt.entity;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    Project project;

    String content;

    @Builder
    public CustomPrompt(Project project, String content) {
        this.project = project;
        this.content = content;
    }

    public void change(String content) {
        this.content = content;
    }
}
