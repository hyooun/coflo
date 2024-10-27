package com.reviewping.coflo.domain.mergerequest.dto.response;

import java.util.List;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabMrDetailContent;
import com.reviewping.coflo.global.common.entity.PageDetail;

import lombok.Builder;

@Builder
public record GitlabMrPageResponse(
	List<GitlabMrDetailContent> gitlabMrList,
	Long totalElements,
	Integer totalPages,
	Boolean isLast,
	Integer currPage
) {
	public static GitlabMrPageResponse of(
		List<GitlabMrDetailContent> gitlabMrList, PageDetail pageDetail) {
		return GitlabMrPageResponse.builder()
			.gitlabMrList(gitlabMrList)
			.totalElements(pageDetail.totalElements())
			.totalPages(pageDetail.totalPages())
			.isLast(pageDetail.isLast())
			.currPage(pageDetail.currPage())
			.build();
	}
}
