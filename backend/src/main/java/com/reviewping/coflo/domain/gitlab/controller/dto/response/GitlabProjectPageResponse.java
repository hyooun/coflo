package com.reviewping.coflo.domain.gitlab.controller.dto.response;

import com.reviewping.coflo.global.client.gitlab.response.PageInfo;
import java.util.List;
import lombok.Builder;

@Builder
public record GitlabProjectPageResponse(
        List<GitlabProjectResponse> gitlabProjectList, PageInfo pageInfo) {}
