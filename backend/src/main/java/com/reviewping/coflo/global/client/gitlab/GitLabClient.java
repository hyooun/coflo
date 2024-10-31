package com.reviewping.coflo.global.client.gitlab;

import static com.reviewping.coflo.global.error.ErrorCode.EXTERNAL_API_BAD_REQUEST;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.domain.gitlab.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.global.client.gitlab.request.GitlabNoteRequest;
import com.reviewping.coflo.global.client.gitlab.response.*;
import com.reviewping.coflo.global.common.entity.PageDetail;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import com.reviewping.coflo.global.util.RestTemplateUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
public class GitLabClient {

    private static final String PRIVATE_TOKEN = "PRIVATE-TOKEN";
    private static final String KST_OFFSET = "+09:00";

    private final ObjectMapper objectMapper;

    public GitlabProjectPageContent searchGitlabProjects(
            String gitlabUrl, String token, GitlabSearchRequest gitlabSearchRequest) {
        HttpHeaders headers = makeGitlabHeaders(token);
        String url =
                GitLabApiUrlBuilder.createSearchGitlabProjectUrl(gitlabUrl, gitlabSearchRequest);

        ResponseEntity<List<GitlabProjectDetailContent>> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<>() {});

        PageDetail pageDetail = createPageDetail(response.getHeaders());
        return new GitlabProjectPageContent(response.getBody(), pageDetail);
    }

    public GitlabMrPageContent searchGitlabMergeRequests(
            String gitlabUrl,
            String token,
            Long gitlabProjectId,
            String mergeRequestState,
            GitlabSearchRequest gitlabSearchRequest,
            LocalDateTime createdAt) {
        HttpHeaders headers = makeGitlabHeaders(token);
        String url =
                GitLabApiUrlBuilder.createSearchMergeRequestUrl(
                        gitlabUrl,
                        gitlabProjectId,
                        mergeRequestState,
                        gitlabSearchRequest,
                        this.convertToGitlabDateFormat(createdAt));
        ResponseEntity<List<GitlabMrDetailContent>> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<>() {});

        PageDetail pageDetail = createPageDetail(response.getHeaders());
        return new GitlabMrPageContent(response.getBody(), pageDetail);
    }

    public GitlabProjectDetailContent getSingleProject(
            String gitlabUrl, String token, Long gitlabProjectId) {
        HttpHeaders headers = makeGitlabHeaders(token);
        String url = GitLabApiUrlBuilder.createSingleProjectUrl(gitlabUrl, gitlabProjectId);
        ResponseEntity<GitlabProjectDetailContent> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<>() {});
        return response.getBody();
    }

    public GitlabUserInfoContent getUserInfo(String gitlabUrl, String token) {
        HttpHeaders headers = makeGitlabHeaders(token);
        String url = GitLabApiUrlBuilder.createUserInfoUrl(gitlabUrl);

        ResponseEntity<GitlabUserInfoContent> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<>() {});
        return response.getBody();
    }

    public List<GitlabMrDiffsContent> getMrDiffs(
            String gitlabUrl, String token, Long gitlabProjectId, Long iid) {
        HttpHeaders headers = makeGitlabHeaders(token);
        String url = GitLabApiUrlBuilder.createMRDiffsUrl(gitlabUrl, gitlabProjectId, iid);

        ResponseEntity<List<GitlabMrDiffsContent>> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<>() {});
        return response.getBody();
    }

    public void addNoteToMr(
            String gitlabUrl, String token, Long gitlabProjectId, Long iid, String chatMessage) {
        HttpHeaders headers = makeGitlabHeaders(token);

        String url = GitLabApiUrlBuilder.createNoteToMRUrl(gitlabUrl, gitlabProjectId, iid);
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

    public ProjectInfoContent getProjectInfoDetail(
            String gitlabUrl, String token, Long gitlabProjectId) {
        int commitCount = getProjectCommitCount(gitlabUrl, token, gitlabProjectId);
        Long branchCount = getProjectBranchCount(gitlabUrl, token, gitlabProjectId);
        Long mergeRequestCount = getProjectMRCount(gitlabUrl, token, gitlabProjectId);
        Map<String, Double> languages = getProjectLanguages(gitlabUrl, token, gitlabProjectId);
        return ProjectInfoContent.of(commitCount, branchCount, mergeRequestCount, languages);
    }

    private int getProjectCommitCount(String gitlabUrl, String token, Long gitlabProjectId) {
        String url = GitLabApiUrlBuilder.createProjectCommitsUrl(gitlabUrl, gitlabProjectId);
        HttpHeaders headers = makeGitlabHeaders(token);

        ResponseEntity<Map<String, Object>> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<>() {});

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("statistics")) {
            throw new BusinessException(EXTERNAL_API_BAD_REQUEST);
        }
        Map<String, Object> statistics = (Map<String, Object>) body.get("statistics");
        return (int) statistics.get("commit_count");
    }

    private Map<String, Double> getProjectLanguages(
            String gitlabUrl, String token, Long gitlabProjectId) {
        String url = GitLabApiUrlBuilder.createProjectLanguagesUrl(gitlabUrl, gitlabProjectId);
        HttpHeaders headers = makeGitlabHeaders(token);
        ResponseEntity<Map<String, Double>> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<>() {});
        return response.getBody();
    }

    private long getProjectBranchCount(String gitlabUrl, String token, Long gitlabProjectId) {
        String url = GitLabApiUrlBuilder.createProjectBranchesUrl(gitlabUrl, gitlabProjectId);
        return getTotalByHeader(token, url);
    }

    private long getProjectMRCount(String gitlabUrl, String token, Long gitlabProjectId) {
        String url = GitLabApiUrlBuilder.createProjectMRUrl(gitlabUrl, gitlabProjectId);
        return getTotalByHeader(token, url);
    }

    private long getTotalByHeader(String token, String url) {
        HttpHeaders headers = makeGitlabHeaders(token);
        ResponseEntity<Object> response =
                RestTemplateUtils.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<>() {});
        return Long.parseLong(Objects.requireNonNull(response.getHeaders().getFirst("X-Total")));
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

    private String convertToGitlabDateFormat(LocalDateTime localDateTime) {
        return localDateTime.toString() + KST_OFFSET;
    }
}
