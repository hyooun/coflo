package com.reviewping.coflo.domain.link.controller.dto.response;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabProjectContent;

import lombok.Builder;

@Builder
public record GitlabProjectResponse(
	Long gitProjectId,
	String name,
	boolean isLinked,
	boolean hasBotToken
) {

	public static GitlabProjectResponse of(GitlabProjectContent content) {
		return GitlabProjectResponse.builder()
			.gitProjectId(content.id())
			.name(content.name())
			.build();
	}
}
