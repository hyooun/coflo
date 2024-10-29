package com.reviewping.coflo.global.client.gitlab.response;

/**
 * @see <a href="https://archives.docs.gitlab.com/17.0/ee/api/merge_requests.html#list-merge-request-diffs">list-merge-request-diffs</a>
 */
public record GitlabMrDiffsContent(String oldPath, String newPath, String diff) {}
