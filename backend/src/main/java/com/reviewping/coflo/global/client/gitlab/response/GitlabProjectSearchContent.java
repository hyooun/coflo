package com.reviewping.coflo.global.client.gitlab.response;

import java.util.List;

public record GitlabProjectSearchContent(
        List<GitlabProjectSimpleContent> nodes, PageInfo pageInfo) {
    public record GitlabProjectSimpleContent(String id, String fullPath) {}
}
