package com.reviewping.coflo.global.client.gitlab.response;

public record GitlabProjectDetailContent(
        Long id, String description, String name, String httpUrlToRepo, String fullPath) {}
