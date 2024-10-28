package com.reviewping.coflo.domain.link.controller.dto.response;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabProjectDetailContent;
import lombok.Builder;

@Builder
public record GitlabProjectResponse(
        Long gitlabProjectId, String name, boolean isLinkable, boolean isLinked) {

    public static GitlabProjectResponse ofNonLinkable(GitlabProjectDetailContent content) {
        return GitlabProjectResponse.builder()
                .gitlabProjectId(content.id())
                .name(content.name())
                .isLinkable(false)
                .isLinked(false)
                .build();
    }

    public static GitlabProjectResponse ofLinkable(
            GitlabProjectDetailContent content, boolean isLinked) {
        return GitlabProjectResponse.builder()
                .gitlabProjectId(content.id())
                .name(content.name())
                .isLinkable(true)
                .isLinked(isLinked)
                .build();
    }
}
