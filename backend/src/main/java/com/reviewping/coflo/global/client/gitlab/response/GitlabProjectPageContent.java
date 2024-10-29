package com.reviewping.coflo.global.client.gitlab.response;

import com.reviewping.coflo.global.common.entity.PageDetail;
import java.util.List;

public record GitlabProjectPageContent(
        List<GitlabProjectDetailContent> gitlabProjectDetailContents, PageDetail pageDetail) {}
