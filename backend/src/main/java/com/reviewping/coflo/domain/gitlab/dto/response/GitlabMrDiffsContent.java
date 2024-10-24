package com.reviewping.coflo.domain.gitlab.dto.response;

import java.util.List;

/**
 * @see <a href="https://archives.docs.gitlab.com/17.0/ee/api/merge_requests.html#list-merge-request-diffs">list-merge-request-diffs</a>
 */
public record GitlabMrDiffsContent(List<Change> changes) {

    public record Change(String oldPath, String newPath, String diff) {}
}
