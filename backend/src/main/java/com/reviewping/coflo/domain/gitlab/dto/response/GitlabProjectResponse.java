package com.reviewping.coflo.domain.gitlab.dto.response;

public record GitlabProjectResponse(
	Long id,
	String description,
	String name
) {
}
