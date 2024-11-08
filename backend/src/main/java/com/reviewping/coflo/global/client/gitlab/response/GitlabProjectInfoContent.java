package com.reviewping.coflo.global.client.gitlab.response;

import com.reviewping.coflo.domain.project.controller.response.LanguageResponse;
import java.util.List;

public record GitlabProjectInfoContent(
        MergeRequests mergeRequests, List<LanguageResponse> languages, Statistics statistics) {

    public record MergeRequests(Long count) {}

    public record Statistics(Integer commitCount) {}
}
