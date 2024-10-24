package com.reviewping.coflo.domain.link.controller.dto.request;

public record GitlabSearchRequest(String keyword, int page, int size) {
    public static GitlabSearchRequest of(String keyword, int page, int size) {
        return new GitlabSearchRequest(keyword, page, size);
    }
}
