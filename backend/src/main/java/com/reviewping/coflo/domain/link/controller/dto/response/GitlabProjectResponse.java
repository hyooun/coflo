package com.reviewping.coflo.domain.link.controller.dto.response;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabProjectContent;
import lombok.Builder;

@Builder
public record GitlabProjectResponse(
        Long gitlabProjectId, String name, boolean isLinkable, boolean isLinked) {

    public static GitlabProjectResponse of(GitlabProjectContent content, boolean isLinkable, boolean isLinked) {
        return GitlabProjectResponse.builder()
                .gitlabProjectId(content.id())
                .name(content.name())
                .isLinkable(isLinkable)
                .isLinked(isLinked)
                .build();
    }
}
