package com.reviewping.coflo.global.client.gitlab.response;

public record GitlabProjectDetailContent(
        String id, String description, String name, String httpUrlToRepo, String fullPath) {}
