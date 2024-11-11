package com.reviewping.coflo.domain.userproject.controller.dto.response;

public record UserProjectStatusResponse(
        Boolean hasLinkedProject, Long projectId, String projectPullPath) {

    public static UserProjectStatusResponse withProjectDetails(
            Long projectId, String projectPullPath) {
        return new UserProjectStatusResponse(true, projectId, projectPullPath);
    }

    public static UserProjectStatusResponse withoutProject() {
        return new UserProjectStatusResponse(false, null, null);
    }
}
