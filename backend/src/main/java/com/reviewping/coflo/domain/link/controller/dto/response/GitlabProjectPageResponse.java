package com.reviewping.coflo.domain.link.controller.dto.response;

import com.reviewping.coflo.global.common.entity.PageDetail;
import java.util.List;
import lombok.Builder;

@Builder
public record GitlabProjectPageResponse(
        List<GitlabProjectResponse> gitlabProjectList,
        long totalElements,
        int totalPages,
        boolean isLast,
        int currPage) {
    public static GitlabProjectPageResponse of(
            List<GitlabProjectResponse> gitlabProjectList, PageDetail pageDetail) {
        return GitlabProjectPageResponse.builder()
                .gitlabProjectList(gitlabProjectList)
                .totalElements(pageDetail.totalElements())
                .totalPages(pageDetail.totalPages())
                .isLast(pageDetail.isLast())
                .currPage(pageDetail.currPage())
                .build();
    }
}
