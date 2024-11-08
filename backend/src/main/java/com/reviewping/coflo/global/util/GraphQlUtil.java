package com.reviewping.coflo.global.util;

import com.reviewping.coflo.domain.gitlab.controller.dto.request.GitlabSearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class GraphQlUtil {

    private static final String HTTPS = "https://";
    private static final String GRAPHQL_ENDPOINT = "/api/graphql";

    private String makeGraphqlUrl(String gitlabUrl) {
        return HTTPS + gitlabUrl + GRAPHQL_ENDPOINT;
    }

    public HttpSyncGraphQlClient getGraphQlClient(String gitlabUrl, String token) {
        RestClient restClient = RestClient.create(makeGraphqlUrl(gitlabUrl));
        return HttpSyncGraphQlClient.builder(restClient)
                .headers((headers) -> headers.setBearerAuth(token))
                .build();
    }

    public String createSingleProjectQuery(Long gitlabProjectId) {
        return String.format(
                "query { projects(membership: true, ids: [\"gid://gitlab/Project/%d\"]) { nodes {"
                        + " id name description httpUrlToRepo fullPath } } }",
                gitlabProjectId);
    }

    public String createSearchProjectQuery(GitlabSearchRequest gitlabSearchRequest) {
        StringBuilder queryBuilder =
                new StringBuilder(
                        "query { projects(membership: true, search: \""
                                + gitlabSearchRequest.keyword()
                                + "\"");
        if (!gitlabSearchRequest.startCursor().isEmpty()) {
            queryBuilder
                    .append(", before: \"")
                    .append(gitlabSearchRequest.startCursor())
                    .append("\"");
            queryBuilder.append(", last: ").append(gitlabSearchRequest.size());
        } else if (!gitlabSearchRequest.endCursor().isEmpty()) {
            queryBuilder.append(", after: \"").append(gitlabSearchRequest.endCursor()).append("\"");
            queryBuilder.append(", first: ").append(gitlabSearchRequest.size());
        } else {
            queryBuilder.append(", first: ").append(gitlabSearchRequest.size());
        }

        queryBuilder
                .append(") {\n")
                .append("    nodes {\n")
                .append("      id\n")
                .append("      fullPath\n")
                .append("    }\n")
                .append("    pageInfo {\n")
                .append("      startCursor\n")
                .append("      hasNextPage\n")
                .append("      hasPreviousPage\n")
                .append("      endCursor\n")
                .append("    }\n")
                .append("  }\n")
                .append("}");

        return queryBuilder.toString();
    }

    public String buildMergeRequestQuery(String fullPath, Long gitMergeRequestIid) {
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder
                .append("query { ")
                .append("project(fullPath: \"")
                .append(fullPath)
                .append("\") { ")
                .append("mergeRequest(iid: \"")
                .append(gitMergeRequestIid)
                .append("\") { ")
                .append("id ")
                .append("iid ")
                .append("title ")
                .append("description ")
                .append("state ")
                .append("mergedAt ")
                .append("createdAt ")
                .append("updatedAt ")
                .append("sourceBranch ")
                .append("targetBranch ")
                .append("labels { nodes { title color } } ")
                .append("assignees(first: 1) { nodes { username name avatarUrl } } ")
                .append("reviewers(first: 1) { nodes { username name avatarUrl } } ")
                .append("} ")
                .append("} ")
                .append("}");

        return queryBuilder.toString();
    }

    public String createUserInfoQuery() {
        return "query { currentUser { username name avatarUrl } }";
    }

    public String createProjectInfoQuery(String fullPath) {
        return String.format(
                "query { project(fullPath: \"%s\") { "
                        + "mergeRequests { count } "
                        + "languages { name color share } "
                        + "statistics { commitCount } "
                        + "} }",
                fullPath);
    }

    public static Long extractIdFromId(String id) {
        int lastSlashIndex = id.lastIndexOf("/");
        return Long.parseLong(id.substring(lastSlashIndex + 1));
    }
}
