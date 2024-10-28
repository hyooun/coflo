package com.reviewping.coflo.domain.userproject.controller.dto.response;

import com.reviewping.coflo.domain.userproject.entity.UserProject;

public record UserProjectResponse(Long projectId, String name) {

    public static UserProjectResponse of(UserProject userProject) {
        return new UserProjectResponse(
                userProject.getProject().getId(), userProject.getProject().getName());
    }
}
