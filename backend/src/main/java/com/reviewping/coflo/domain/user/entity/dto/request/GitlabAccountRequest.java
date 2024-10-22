package com.reviewping.coflo.domain.user.entity.dto.request;

public record GitlabAccountRequest(
	String domain,
	String userToken
) {
}
