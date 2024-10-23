package com.reviewping.coflo.domain.link.controller.dto.request;

public record GitlabSearchRequest(String keyword, Long page, Long size) {
    public static GitlabSearchRequest of(String keyword, Long page, Long size) {
        return new GitlabSearchRequest(keyword, page, size);
    }
}
