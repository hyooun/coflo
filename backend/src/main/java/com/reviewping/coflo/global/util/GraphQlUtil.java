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
                "query { "
                        + "  projects(membership: true, ids: [\"gid://gitlab/Project/%d\"]) { "
                        + "    nodes { "
                        + "      id "
                        + "      name "
                        + "      description "
                        + "      httpUrlToRepo "
                        + "      fullPath "
                        + "    } "
                        + "  } "
                        + "}",
                gitlabProjectId);
    }

    public String createSearchProjectQuery(GitlabSearchRequest gitlabSearchRequest) {
        return String.format(
                "query { "
                        + "  projects(membership: true, search: \"%s\"%s) { "
                        + "    nodes { "
                        + "      id "
                        + "      fullPath "
                        + "    } "
                        + "    pageInfo { "
                        + "      startCursor "
                        + "      hasNextPage "
                        + "      hasPreviousPage "
                        + "      endCursor "
                        + "    } "
                        + "  } "
                        + "}",
                gitlabSearchRequest.keyword(), createPagination(gitlabSearchRequest));
    }

    private String createPagination(GitlabSearchRequest gitlabSearchRequest) {
        if (!gitlabSearchRequest.startCursor().isEmpty()) {
            return String.format(
                    ", before: \"%s\", last: %d",
                    gitlabSearchRequest.startCursor(), gitlabSearchRequest.size());
        } else if (!gitlabSearchRequest.endCursor().isEmpty()) {
            return String.format(
                    ", after: \"%s\", first: %d",
                    gitlabSearchRequest.endCursor(), gitlabSearchRequest.size());
        } else {
            return String.format(", first: %d", gitlabSearchRequest.size());
        }
    }

    public String buildMergeRequestQuery(String fullPath, Long gitMergeRequestIid) {
        return String.format(
                "query { "
                        + "  project(fullPath: \"%s\") { "
                        + "    mergeRequest(iid: \"%d\") { "
                        + "      id "
                        + "      iid "
                        + "      title "
                        + "      description "
                        + "      state "
                        + "      mergedAt "
                        + "      createdAt "
                        + "      updatedAt "
                        + "      sourceBranch "
                        + "      targetBranch "
                        + "      labels { "
                        + "        nodes { "
                        + "          title "
                        + "          color "
                        + "        } "
                        + "      } "
                        + "      assignees(first: 1) { "
                        + "        nodes { "
                        + "          username "
                        + "          name "
                        + "          avatarUrl "
                        + "        } "
                        + "      } "
                        + "      reviewers(first: 1) { "
                        + "        nodes { "
                        + "          username "
                        + "          name "
                        + "          avatarUrl "
                        + "        } "
                        + "      } "
                        + "    } "
                        + "  } "
                        + "}",
                fullPath, gitMergeRequestIid);
    }

    public String createUserInfoQuery() {
        return "query { currentUser { username name avatarUrl } }";
    }

    public String createProjectInfoQuery(String fullPath) {
        return String.format(
                "query { "
                        + "  project(fullPath: \"%s\") { "
                        + "    mergeRequests { count } "
                        + "    languages { name color share } "
                        + "    statistics { commitCount } "
                        + "  } "
                        + "}",
                fullPath);
    }

    public static Long extractIdFromId(String id) {
        int lastSlashIndex = id.lastIndexOf("/");
        return Long.parseLong(id.substring(lastSlashIndex + 1));
    }
}
