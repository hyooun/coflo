package com.reviewping.coflo.domain.link.controller.dto;

import com.reviewping.coflo.domain.link.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.domain.link.controller.dto.response.GitlabProjectPageResponse;
import com.reviewping.coflo.domain.link.service.LinkService;
import com.reviewping.coflo.global.auth.oauth.model.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/link")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @GetMapping("/search")
    public ApiResponse<GitlabProjectPageResponse> getGitlabProjects(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        GitlabProjectPageResponse gitlabProjects =
                linkService.getGitlabProjects(
                        authUser.getUserId(), new GitlabSearchRequest(keyword, page, size));
        return ApiSuccessResponse.success(gitlabProjects);
    }
}
