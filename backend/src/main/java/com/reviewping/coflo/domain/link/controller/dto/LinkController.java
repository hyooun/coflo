package com.reviewping.coflo.domain.link.controller.dto;

import com.reviewping.coflo.domain.link.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.domain.link.controller.dto.request.ProjectLinkRequest;
import com.reviewping.coflo.domain.link.controller.dto.response.GitlabProjectPageResponse;
import com.reviewping.coflo.domain.link.service.LinkService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/link")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @GetMapping("/search")
    public ApiResponse<GitlabProjectPageResponse> getGitlabProjects(
            @AuthUser User user,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        GitlabProjectPageResponse gitlabProjects =
                linkService.getGitlabProjects(
                        user.getId(), new GitlabSearchRequest(keyword, page, size));
        return ApiSuccessResponse.success(gitlabProjects);
    }

    @PostMapping("/{gitlabProjectId}")
    public ApiResponse<Map<String, Long>> linkGitlabProject(
            @AuthUser User user,
            @PathVariable("gitlabProjectId") Long gitlabProjectId,
            @RequestBody(required = false) ProjectLinkRequest projectLinkRequest) {
        Long projectId =
                linkService.linkGitlabProject(user.getId(), gitlabProjectId, projectLinkRequest);
        return ApiSuccessResponse.success("projectId", projectId);
    }

    @GetMapping("/status")
    public ApiResponse<Map<String, Boolean>> linkStatus(
            @AuthenticationPrincipal AuthUser authUser) {
        boolean hasLinkedProject = linkService.hasLinkedProject(authUser.getUserId());
        return ApiSuccessResponse.success("hasLinkedProject", hasLinkedProject);
    }
}
