package com.reviewping.coflo.domain.project.controller.dto.response;

import com.reviewping.coflo.domain.project.entity.Project;

public record ProjectResponse(Long id, String name) {

    public static ProjectResponse of(Project project) {
        return new ProjectResponse(project.getId(), project.getName());
    }
}
