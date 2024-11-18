package com.reviewping.coflo.domain.gitlab.controller.dto.response;

import com.reviewping.coflo.global.client.gitlab.response.GitlabProjectSearchContent.GitlabProjectSimpleContent;
import com.reviewping.coflo.global.util.GraphQlUtil;
import lombok.Builder;

@Builder
public record GitlabProjectResponse(
        Long gitlabProjectId, String name, boolean isLinkable, boolean isLinked) {

    public static GitlabProjectResponse ofNonLinkable(GitlabProjectSimpleContent content) {
        return GitlabProjectResponse.builder()
                .gitlabProjectId(GraphQlUtil.extractIdFromId(content.id()))
                .name(content.fullPath())
                .isLinkable(false)
                .isLinked(false)
                .build();
    }

    public static GitlabProjectResponse ofLinkable(
            GitlabProjectSimpleContent content, boolean isLinked) {
        return GitlabProjectResponse.builder()
                .gitlabProjectId(GraphQlUtil.extractIdFromId(content.id()))
                .name(content.fullPath())
                .isLinkable(true)
                .isLinked(isLinked)
                .build();
    }
}
