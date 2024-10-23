package com.reviewping.coflo.domain.gitlab.service;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabProjectContent;
import com.reviewping.coflo.domain.gitlab.dto.response.GitlabUserInfoContent;
import com.reviewping.coflo.domain.link.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.global.util.RestTemplateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitLabApiService {

    @Value("${gitlab.api.user-info}")
    private String userInfoEndpoint;

    private static final String SEARCH_ENDPOINT = "/api/v4/search";
    private static final String USERINFO_ENDPOINT = "/api/v4/user";

    private static final String URL_PROTOCOL_HTTPS = "https://";
    private static final String MIME_TYPE_JSON = "application/json";

    public List<GitlabProjectContent> searchGitlabProjects(
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

        ResponseEntity<List<GitlabProjectContent>> response =
                RestTemplateUtils.sendGetRequest(
                        url,
                        headers,
                        new ParameterizedTypeReference<List<GitlabProjectContent>>() {
                        });
        return response.getBody();
    }

    public GitlabUserInfoContent getUserInfo(String gitlabUrl, String token) {
        HttpHeaders headers = RestTemplateUtils.createHeaders(MIME_TYPE_JSON, token);
        String url = URL_PROTOCOL_HTTPS + gitlabUrl + USERINFO_ENDPOINT;

        ResponseEntity<GitlabUserInfoContent> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<GitlabUserInfoContent>() {
                        });
        return response.getBody();
    }
}
