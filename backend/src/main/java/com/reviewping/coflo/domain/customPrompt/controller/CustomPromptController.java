package com.reviewping.coflo.domain.customPrompt.controller;

import com.reviewping.coflo.domain.customPrompt.controller.dto.request.CustomPromptRequest;
import com.reviewping.coflo.domain.customPrompt.controller.dto.response.CustomPromptResponse;
import com.reviewping.coflo.domain.customPrompt.service.CustomPromptService;
import com.reviewping.coflo.domain.customPrompt.service.GetCustomPromptService;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/custom-prompts")
@RequiredArgsConstructor
public class CustomPromptController {

    private final CustomPromptService customPromptService;
    private final GetCustomPromptService getCustomPromptService;

    @GetMapping("/{projectId}")
    public ApiResponse<CustomPromptResponse> getCustomPrompt(
            @PathVariable("projectId") Long projectId) {
        return ApiSuccessResponse.success(getCustomPromptService.getCustomPrompt(projectId));
    }

    @PutMapping("/{customPromptId}")
    public ApiResponse<Void> updateCustomPrompt(
            @PathVariable("customPromptId") Long customPromptId,
            @ModelAttribute CustomPromptRequest customPromptRequest) {
        customPromptService.updateCustomPrompt(customPromptRequest.contents(), customPromptId);
        return ApiSuccessResponse.success();
    }
}
