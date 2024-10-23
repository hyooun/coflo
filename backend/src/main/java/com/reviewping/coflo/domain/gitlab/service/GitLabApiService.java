package com.reviewping.coflo.domain.gitlab.service;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabProjectDetailContent;
import com.reviewping.coflo.domain.gitlab.dto.response.GitlabProjectPageContent;
import com.reviewping.coflo.domain.gitlab.dto.response.GitlabUserInfoContent;
import com.reviewping.coflo.domain.link.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.global.common.entity.PageDetail;
import com.reviewping.coflo.global.util.RestTemplateUtils;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitLabApiService {

    private static final String SEARCH_ENDPOINT = "/api/v4/search";
    private static final String USERINFO_ENDPOINT = "/api/v4/user";

    private static final String URL_PROTOCOL_HTTPS = "https://";
    private static final String MIME_TYPE_JSON = "application/json";

    public GitlabProjectPageContent searchGitlabProjects(
            String gitlabUrl, String token, GitlabSearchRequest gitlabSearchRequest) {
        HttpHeaders headers = RestTemplateUtils.createHeaders(MIME_TYPE_JSON, token);
        String url =
                URL_PROTOCOL_HTTPS
                        + gitlabUrl
                        + SEARCH_ENDPOINT
                        + "?scope=projects&search="
                        + gitlabSearchRequest.keyword()
                        + "&page="
                        + gitlabSearchRequest.page()
                        + "&per_page="
                        + gitlabSearchRequest.size();

        ResponseEntity<List<GitlabProjectDetailContent>> response =
                RestTemplateUtils.sendGetRequest(
                        url,
                        headers,
                        new ParameterizedTypeReference<List<GitlabProjectDetailContent>>() {});

        PageDetail pageDetail = createPageDetail(response.getHeaders());
        return GitlabProjectPageContent.of(response.getBody(), pageDetail);
    }

    public GitlabUserInfoContent getUserInfo(String gitlabUrl, String token) {
        HttpHeaders headers = RestTemplateUtils.createHeaders(MIME_TYPE_JSON, token);
        String url = URL_PROTOCOL_HTTPS + gitlabUrl + USERINFO_ENDPOINT;

        ResponseEntity<GitlabUserInfoContent> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<GitlabUserInfoContent>() {});
        return response.getBody();
    }

    private PageDetail createPageDetail(HttpHeaders responseHeaders) {
        long totalElements =
                Long.parseLong(Objects.requireNonNull(responseHeaders.getFirst("X-Total")));
        int totalPages =
                Integer.parseInt(Objects.requireNonNull(responseHeaders.getFirst("X-Total-Pages")));
        int currPage = Integer.parseInt(Objects.requireNonNull(responseHeaders.getFirst("X-Page")));
        boolean isLast = currPage == totalPages;
        return PageDetail.of(totalElements, totalPages, isLast, currPage);
    }
}
