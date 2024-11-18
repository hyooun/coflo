package com.reviewping.coflo.domain.user.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GitlabAccountRequest(@NotBlank String domain, @NotBlank String userToken) {}
