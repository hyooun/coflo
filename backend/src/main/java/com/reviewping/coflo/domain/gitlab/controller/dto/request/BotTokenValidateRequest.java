package com.reviewping.coflo.domain.gitlab.controller.dto.request;

public record BotTokenValidateRequest(Long gitlabProjectId, String botToken) {}
