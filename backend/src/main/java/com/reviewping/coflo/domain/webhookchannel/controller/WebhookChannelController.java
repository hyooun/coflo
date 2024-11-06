package com.reviewping.coflo.domain.webhookchannel.controller;

import com.reviewping.coflo.domain.webhookchannel.controller.dto.request.UpdateWebhookChannelRequest;
import com.reviewping.coflo.domain.webhookchannel.controller.dto.request.WebhookChannelRequest;
import com.reviewping.coflo.domain.webhookchannel.controller.dto.response.WebhookChannelResponse;
import com.reviewping.coflo.domain.webhookchannel.service.WebhookChannelService;
import com.reviewping.coflo.global.aop.LogExecution;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@LogExecution
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class WebhookChannelController {

    private final WebhookChannelService webhookChannelService;
    private static final String TEST_CONTENT = "Hello coFlo!";

    @PostMapping
    @Operation(summary = "웹훅 채널 신규 등록")
    public ApiResponse<Void> addWebhookChannel(
            @RequestBody WebhookChannelRequest webhookChannelRequest) {
        webhookChannelService.addWebhookChannel(
                webhookChannelRequest.projectId(),
                webhookChannelRequest.channelCodeId(),
                webhookChannelRequest.webhookUrl());

        return ApiSuccessResponse.success();
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "등록된 웹훅 채널 목록 조회")
    public ApiResponse<List<WebhookChannelResponse>> getWebhookChannelList(
            @PathVariable("projectId") Long projectId) {
        return ApiSuccessResponse.success(webhookChannelService.getWebhookChannelList(projectId));
    }

    @PostMapping("/test/{projectId}")
    @Operation(summary = "웹훅 연동 테스트")
    public ApiResponse<Void> testWebhookChannel(@PathVariable("projectId") Long projectId) {
        webhookChannelService.sendData(projectId, TEST_CONTENT);
        return ApiSuccessResponse.success();
    }

    @PatchMapping("/{webhookChannelId}")
    @Operation(summary = "연동된 웹훅 정보 수정")
    public ApiResponse<Void> updateWebhookChannel(
            @PathVariable("webhookChannelId") Long webhookChannelId,
            @RequestBody UpdateWebhookChannelRequest updateWebhookChannelRequest) {
        webhookChannelService.updateWebhookChannel(
                webhookChannelId, updateWebhookChannelRequest.webhookUrl());
        return ApiSuccessResponse.success();
    }

    @DeleteMapping("/{webhookChannelId}")
    @Operation(summary = "연동된 웹훅 연동 해제")
    public ApiResponse<Void> deleteWebhookChannel(
            @PathVariable("webhookChannelId") Long webhookChannelId) {
        webhookChannelService.deleteWebhookChannel(webhookChannelId);
        return ApiSuccessResponse.success();
    }
}
