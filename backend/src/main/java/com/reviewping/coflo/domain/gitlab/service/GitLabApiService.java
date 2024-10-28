package com.reviewping.coflo.domain.gitlab.service;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.domain.gitlab.dto.response.*;
import com.reviewping.coflo.domain.link.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.global.common.entity.PageDetail;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import com.reviewping.coflo.global.util.RestTemplateUtils;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitLabApiService {

    private static final String PRIVATE_TOKEN = "PRIVATE-TOKEN";

    private static final String PROJECTS_ENDPOINT =
            "/api/v4/projects?order_by=updated_at&sort=desc";
    private static final String USERINFO_ENDPOINT = "/api/v4/user";
    private static final String SINGLE_PROJECT_ENDPOINT = "/api/v4/projects/";

    private static final String URL_PROTOCOL_HTTPS = "https://";
    private static final String MIME_TYPE_JSON = "application/json";

    private final ObjectMapper objectMapper;

    public void validateToken(String domain, String token) {
        searchGitlabProjects(domain, token, new GitlabSearchRequest("", 1, 20));
    }

    public GitlabProjectPageContent searchGitlabProjects(
            String gitlabUrl, String token, GitlabSearchRequest gitlabSearchRequest) {
        HttpHeaders headers = RestTemplateUtils.createHeaders(MIME_TYPE_JSON, token);
        String url = makeSearchGitlabProjectUrl(gitlabUrl, gitlabSearchRequest);

        ResponseEntity<List<GitlabProjectDetailContent>> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<>() {});

        PageDetail pageDetail = createPageDetail(response.getHeaders());
        return new GitlabProjectPageContent(response.getBody(), pageDetail);
    }

    public GitlabProjectDetailContent getSingleProject(
            String gitlabUrl, String token, Long gitlabProjectId) {
        HttpHeaders headers = RestTemplateUtils.createHeaders(MIME_TYPE_JSON, token);
        String url = URL_PROTOCOL_HTTPS + gitlabUrl + SINGLE_PROJECT_ENDPOINT + gitlabProjectId;
        ResponseEntity<GitlabProjectDetailContent> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<>() {});
        return response.getBody();
    }

    public GitlabUserInfoContent getUserInfo(String gitlabUrl, String token) {
        HttpHeaders headers = RestTemplateUtils.createHeaders(MIME_TYPE_JSON, token);
        String url = URL_PROTOCOL_HTTPS + gitlabUrl + USERINFO_ENDPOINT;

        ResponseEntity<GitlabUserInfoContent> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<>() {});
        return response.getBody();
    }

    public List<GitlabMrDiffsContent> getMrDiffs(
            String gitlabUrl, String token, Long gitlabProjectId, Long iid) {
        HttpHeaders headers = makeGitlabHeaders(token);
        String url = makeMRDiffsUrl(gitlabUrl, gitlabProjectId, iid);

        ResponseEntity<List<GitlabMrDiffsContent>> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<>() {});
        return response.getBody();
    }

    public void addNoteToMr(
            String gitlabUrl, String token, Long gitlabProjectId, Long iid, String chatMessage) {
        HttpHeaders headers = makeGitlabHeaders(token);

        String url = makeNoteToMRUrl(gitlabUrl, gitlabProjectId, iid);
        GitlabNoteRequest gitlabNoteRequest = new GitlabNoteRequest(chatMessage);
        String body;
        try {
            body = objectMapper.writeValueAsString(gitlabNoteRequest);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.GITLAB_REQUEST_SERIALIZATION_ERROR);
        }
        RestTemplateUtils.sendPostRequest(
                url, headers, body, new ParameterizedTypeReference<>() {});
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

    private HttpHeaders makeGitlabHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.set(PRIVATE_TOKEN, token);
        return headers;
    }

    private String makeSearchGitlabProjectUrl(
            String gitlabUrl, GitlabSearchRequest gitlabSearchRequest) {
        return URL_PROTOCOL_HTTPS
                + gitlabUrl
                + PROJECTS_ENDPOINT
                + "&search="
                + gitlabSearchRequest.keyword()
                + "&page="
                + gitlabSearchRequest.page()
                + "&per_page="
                + gitlabSearchRequest.size();
    }

    private String makeMRDiffsUrl(String gitlabUrl, Long gitlabProjectId, Long iid) {
        return URL_PROTOCOL_HTTPS
                + gitlabUrl
                + "/api/v4/projects/"
                + gitlabProjectId
                + "/merge_requests/"
                + iid
                + "/diffs";
    }

    private String makeNoteToMRUrl(String gitlabUrl, Long gitlabProjectId, Long iid) {
        return URL_PROTOCOL_HTTPS
                + gitlabUrl
                + "/api/v4/projects/"
                + gitlabProjectId
                + "/merge_requests/"
                + iid
                + "/notes";
    }
}
