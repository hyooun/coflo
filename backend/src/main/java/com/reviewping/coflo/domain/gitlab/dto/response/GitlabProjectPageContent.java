package com.reviewping.coflo.domain.gitlab.dto.response;

import com.reviewping.coflo.global.common.entity.PageDetail;
import java.util.List;

public record GitlabProjectPageContent(
        List<GitlabProjectDetailContent> gitlabProjectDetailContents, PageDetail pageDetail) {

    public static GitlabProjectPageContent of(
            List<GitlabProjectDetailContent> gitlabProjectDetailContents, PageDetail pageDetail) {
        return new GitlabProjectPageContent(gitlabProjectDetailContents, pageDetail);
    }
}
