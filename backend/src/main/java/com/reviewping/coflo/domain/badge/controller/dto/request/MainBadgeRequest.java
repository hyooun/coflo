package com.reviewping.coflo.domain.badge.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record MainBadgeRequest(@NotNull Long badgeCodeId) {}
