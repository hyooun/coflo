package com.reviewping.coflo.global.client.gitlab.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @see <a href="https://archives.docs.gitlab.com/17.0/ee/api/merge_requests.html#list-merge-request-diffs">list-merge-request-diffs</a>
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GitlabMrDiffsContent(String oldPath, String newPath, String diff) {}
