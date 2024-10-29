package com.reviewping.coflo.domain.gitlab.controller.dto.request;

public record GitlabSearchRequest(String keyword, int page, int size) {}
