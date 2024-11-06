package com.reviewping.coflo.domain.customprompt.controller;

import com.reviewping.coflo.domain.customprompt.controller.dto.request.CustomPromptRequest;
import com.reviewping.coflo.domain.customprompt.controller.dto.response.CustomPromptResponse;
import com.reviewping.coflo.domain.customprompt.service.CustomPromptService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.aop.LogExecution;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@LogExecution
@RestController
@RequestMapping("/api/custom-prompts")
@RequiredArgsConstructor
public class CustomPromptController {

    private final CustomPromptService customPromptService;

    @GetMapping("/{projectId}")
    @Operation(summary = "연동된 프로젝트의 Custom Prompt 조회")
    public ApiResponse<CustomPromptResponse> getCustomPrompt(
            @PathVariable("projectId") Long projectId) {
        return ApiSuccessResponse.success(customPromptService.getCustomPrompt(projectId));
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "연동된 프로젝트의 Custom Prompt 수정")
    public ApiResponse<Void> updateCustomPrompt(
            @AuthUser User user,
            @PathVariable("projectId") Long projectId,
            @ModelAttribute CustomPromptRequest customPromptRequest) {
        customPromptService.updateCustomPrompt(user, customPromptRequest.contents(), projectId);
        return ApiSuccessResponse.success();
    }
}
