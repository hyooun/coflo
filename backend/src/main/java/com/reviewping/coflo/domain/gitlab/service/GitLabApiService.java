package com.reviewping.coflo.domain.gitlab.service;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabProjectResponse;
import com.reviewping.coflo.domain.gitlab.dto.response.GitlabUserInfoResponse;
import com.reviewping.coflo.global.util.RestTemplateUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitLabApiService {

    @Value("${gitlab.api.project-list}")
    private String projectListEndpoint;

    @Value("${gitlab.api.user-info}")
    private String userInfoEndpoint;

    public List<GitlabProjectResponse> getProjects(String gitlabUrl, String token) {
        HttpHeaders headers = RestTemplateUtils.createHeaders("application/json", token);
        String url = gitlabUrl + projectListEndpoint;

        ResponseEntity<List<GitlabProjectResponse>> response =
                RestTemplateUtils.sendGetRequest(
                        url,
                        headers,
                        new ParameterizedTypeReference<List<GitlabProjectResponse>>() {});
        return response.getBody();
    }

    public GitlabUserInfoResponse getUserInfo(String gitlabUrl, String token) {
        HttpHeaders headers = RestTemplateUtils.createHeaders("application/json", token);
        String url = "https://" + gitlabUrl + userInfoEndpoint;

        ResponseEntity<GitlabUserInfoResponse> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<GitlabUserInfoResponse>() {});
        return response.getBody();
    }
}
