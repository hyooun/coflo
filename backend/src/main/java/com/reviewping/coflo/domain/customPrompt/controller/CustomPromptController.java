package com.reviewping.coflo.domain.customPrompt.controller;

import com.reviewping.coflo.domain.customPrompt.controller.dto.request.CustomPromptRequest;
import com.reviewping.coflo.domain.customPrompt.controller.dto.response.CustomPromptResponse;
import com.reviewping.coflo.domain.customPrompt.service.CustomPromptService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/custom-prompts")
@RequiredArgsConstructor
public class CustomPromptController {

    private final CustomPromptService customPromptService;

    @GetMapping("/{projectId}")
    public ApiResponse<CustomPromptResponse> getCustomPrompt(
            @PathVariable("projectId") Long projectId) {
        return ApiSuccessResponse.success(customPromptService.getCustomPrompt(projectId));
    }

    @PutMapping("/{customPromptId}")
    public ApiResponse<Void> updateCustomPrompt(
            @AuthUser User user,
            @PathVariable("customPromptId") Long customPromptId,
            @ModelAttribute CustomPromptRequest customPromptRequest) {
        customPromptService.updateCustomPrompt(
                user, customPromptRequest.contents(), customPromptId);
        return ApiSuccessResponse.success();
    }
}
