package com.reviewping.coflo.global.client.gitlab;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.domain.gitlab.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.domain.mergerequest.controller.dto.request.GitlabMrPageRequest;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrResponse;
import com.reviewping.coflo.domain.mergerequest.entity.MrInfo;
import com.reviewping.coflo.global.client.gitlab.request.GitlabNoteRequest;
import com.reviewping.coflo.global.client.gitlab.response.*;
import com.reviewping.coflo.global.common.entity.PageDetail;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import com.reviewping.coflo.global.util.GraphQlUtil;
import com.reviewping.coflo.global.util.RestTemplateUtil;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitLabClient {

    private static final String PRIVATE_TOKEN = "PRIVATE-TOKEN";

    private final RestTemplateUtil restTemplateUtil;
    private final GraphQlUtil graphqlUtil;
    private final ObjectMapper objectMapper;

    public GitlabProjectSearchContent searchGitlabProjects(
            String gitlabUrl, String token, GitlabSearchRequest gitlabSearchRequest) {
        HttpSyncGraphQlClient graphQlClient = graphqlUtil.getGraphQlClient(gitlabUrl, token);
        ClientGraphQlResponse response =
                graphQlClient
                        .document(graphqlUtil.createSearchProjectQuery(gitlabSearchRequest))
                        .executeSync();
        return response.field("projects").toEntity(GitlabProjectSearchContent.class);
    }

    public GitlabMrPageContent searchGitlabMergeRequests(
            String gitlabUrl,
            String token,
            Long gitlabProjectId,
            GitlabMrPageRequest request,
            LocalDateTime createdAt) {
        HttpHeaders headers = makeGitlabHeaders(token);
        String url =
                GitLabApiUrlBuilder.createSearchMergeRequestUrl(
                        gitlabUrl, gitlabProjectId, request, createdAt.toString() + "09:00");
        ResponseEntity<List<GitlabMrDetailContent>> response =
                restTemplateUtil.sendGetRequest(
                        url, headers, new ParameterizedTypeReference<>() {});

        PageDetail pageDetail = createPageDetail(response.getHeaders());
        return new GitlabMrPageContent(response.getBody(), pageDetail);
    }

    public GitlabMrDetailContent getSingleMergeRequest(
            String gitlabUrl, String token, String fullPath, Long mergeRequestIid) {
        HttpSyncGraphQlClient graphQlClient = graphqlUtil.getGraphQlClient(gitlabUrl, token);
        ClientGraphQlResponse response =
                graphQlClient
                        .document(graphqlUtil.buildMergeRequestQuery(fullPath, mergeRequestIid))
                        .executeSync();
        return response.field("project.mergeRequest").toEntity(GitlabMrDetailContent.class);
    }

    public GitlabProjectDetailContent getSingleProject(
            String gitlabUrl, String token, Long gitlabProjectId) {
        HttpSyncGraphQlClient graphQlClient = graphqlUtil.getGraphQlClient(gitlabUrl, token);
        ClientGraphQlResponse response =
                graphQlClient
                        .document(graphqlUtil.createSingleProjectQuery(gitlabProjectId))
                        .executeSync();
        List<GitlabProjectDetailContent> projects =
                response.field("projects.nodes").toEntityList(GitlabProjectDetailContent.class);
        return projects.isEmpty() ? null : projects.getFirst();
    }

    public GitlabUserInfoContent getUserInfo(String gitlabUrl, String token) {
        HttpSyncGraphQlClient graphQlClient = graphqlUtil.getGraphQlClient(gitlabUrl, token);
        ClientGraphQlResponse response =
                graphQlClient.document(graphqlUtil.createUserInfoQuery()).executeSync();
        return response.field("currentUser").toEntity(GitlabUserInfoContent.class);
    }

    public List<GitlabMrResponse> getTop3MrList(
            String gitlabUrl, String token, String fullPath, List<MrInfo> mrInfoList) {
        List<GitlabMrResponse> top3MrList = new ArrayList<>();
        for (MrInfo mrInfo : mrInfoList) {
            GitlabMrResponse singleMergeRequest =
                    GitlabMrResponse.of(
                            getSingleMergeRequest(
                                    gitlabUrl, token, fullPath, mrInfo.getGitlabMrIid()),
                            false);
            top3MrList.add(singleMergeRequest);
        }
        return top3MrList;
    }

    public List<GitlabMrDiffsContent> getMrDiffs(
            String gitlabUrl, String token, Long gitlabProjectId, Long iid) {
        HttpHeaders headers = makeGitlabHeaders(token);
        String url = GitLabApiUrlBuilder.createMRDiffsUrl(gitlabUrl, gitlabProjectId, iid);

        ResponseEntity<List<GitlabMrDiffsContent>> response =
                restTemplateUtil.sendGetRequest(
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
            throw new BusinessException(ErrorCode.GITLAB_REQUEST_SERIALIZATION_ERROR, e);
        }
        restTemplateUtil.sendPostRequest(url, headers, body, new ParameterizedTypeReference<>() {});
    }

    public ProjectInfoContent getProjectInfoDetail(
            String gitlabUrl, String token, Long gitlabProjectId, String fullPath) {
        HttpSyncGraphQlClient graphQlClient = graphqlUtil.getGraphQlClient(gitlabUrl, token);
        ClientGraphQlResponse response =
                graphQlClient.document(graphqlUtil.createProjectInfoQuery(fullPath)).executeSync();
        GitlabProjectInfoContent content =
                response.field("project").toEntity(GitlabProjectInfoContent.class);
        Long branchCount = getProjectBranchCount(gitlabUrl, token, gitlabProjectId);
        return ProjectInfoContent.of(
                content.statistics().commitCount(),
                branchCount,
                content.mergeRequests().count(),
                content.languages());
    }

    public void addProjectWebhook(
            String gitlabUrl,
            String token,
            Long gitlabProjectId,
            String webhookUrl,
            Map<String, Boolean> eventSettings) {
        HttpHeaders headers = makeGitlabHeaders(token);
        String url = GitLabApiUrlBuilder.createProjectWebhookUrl(gitlabUrl, gitlabProjectId);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("url", webhookUrl);
        requestBody.putAll(eventSettings);

        String body;
        try {
            body = objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.GITLAB_REQUEST_SERIALIZATION_ERROR, e);
        }

        restTemplateUtil.sendPostRequest(url, headers, body, new ParameterizedTypeReference<>() {});
    }

    public List<String> getAllBranchNames(String gitlabUrl, String token, Long gitlabProjectId) {
        HttpHeaders headers = makeGitlabHeaders(token);
        String branchUrl = GitLabApiUrlBuilder.createProjectBranchesUrl(gitlabUrl, gitlabProjectId);

        int page = 1;
        int totalPages = 0;
        List<String> allBranches = new ArrayList<>();
        do {
            String url = branchUrl + "?page=" + page + "&per_page=100";
            ResponseEntity<List<GitlabBranchContent>> response =
                    restTemplateUtil.sendGetRequest(
                            url, headers, new ParameterizedTypeReference<>() {});

            List<GitlabBranchContent> branches = response.getBody();
            if (branches != null) {
                branches.forEach(branch -> allBranches.add(branch.name()));
            }

            if (totalPages == 0) {
                totalPages = getTotalPages(response.getHeaders());
            }
            page++;
        } while (page <= totalPages);

        return allBranches;
    }

    private long getProjectBranchCount(String gitlabUrl, String token, Long gitlabProjectId) {
        String url =
                GitLabApiUrlBuilder.createProjectBranchesUrl(gitlabUrl, gitlabProjectId)
                        + "?per_page=1";
        return getTotalByHeader(token, url);
    }

    private long getTotalByHeader(String token, String url) {
        HttpHeaders headers = makeGitlabHeaders(token);
        ResponseEntity<Object> response =
                restTemplateUtil.sendGetRequest(
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

    private static int getTotalPages(HttpHeaders responseHeaders) {
        return Integer.parseInt(Objects.requireNonNull(responseHeaders.getFirst("X-Total-Pages")));
    }
}
