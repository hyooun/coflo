package com.reviewping.coflo.domain.user.dto.request;

public record GitlabAccountRequest(
	String domain,
	String userToken
) {
}
