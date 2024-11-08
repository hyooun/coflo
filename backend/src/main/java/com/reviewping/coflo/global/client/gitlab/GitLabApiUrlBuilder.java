package com.reviewping.coflo.global.client.gitlab;

import com.reviewping.coflo.domain.mergerequest.controller.dto.request.GitlabMrPageRequest;

public class GitLabApiUrlBuilder {

    private static final String URL_PROTOCOL_HTTPS = "https://";
    private static final String SINGLE_PROJECT_ENDPOINT = "/api/v4/projects/";

    public static String createMRDiffsUrl(String gitlabUrl, Long gitlabProjectId, Long iid) {
        return URL_PROTOCOL_HTTPS
                + gitlabUrl
                + SINGLE_PROJECT_ENDPOINT
                + gitlabProjectId
                + "/merge_requests/"
                + iid
                + "/diffs";
    }

    public static String createNoteToMRUrl(String gitlabUrl, Long gitlabProjectId, Long iid) {
        return URL_PROTOCOL_HTTPS
                + gitlabUrl
                + SINGLE_PROJECT_ENDPOINT
                + gitlabProjectId
                + "/merge_requests/"
                + iid
                + "/notes";
    }

    public static String createSearchMergeRequestUrl(
            String gitlabUrl, Long gitlabProjectId, GitlabMrPageRequest request, String createAt) {
        return URL_PROTOCOL_HTTPS
                + gitlabUrl
                + SINGLE_PROJECT_ENDPOINT
                + gitlabProjectId
                + "/merge_requests"
                + "?state="
                + request.state()
                + "&search="
                + request.keyword()
                + "&page="
                + request.page()
                + "&per_page="
                + request.size()
                + "&created_after="
                + createAt;
    }

    public static String createProjectBranchesUrl(String gitlabUrl, Long gitlabProjectId) {
        return URL_PROTOCOL_HTTPS
                + gitlabUrl
                + SINGLE_PROJECT_ENDPOINT
                + gitlabProjectId
                + "/repository/branches";
    }

    public static String createProjectWebhookUrl(String gitlabUrl, Long gitlabProjectId) {
        return URL_PROTOCOL_HTTPS
                + gitlabUrl
                + SINGLE_PROJECT_ENDPOINT
                + gitlabProjectId
                + "/hooks";
    }
}
