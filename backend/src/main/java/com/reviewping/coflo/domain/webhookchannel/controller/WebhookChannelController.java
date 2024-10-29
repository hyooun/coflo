package com.reviewping.coflo.domain.webhookchannel.controller;

import com.reviewping.coflo.domain.webhookchannel.controller.dto.request.WebhookChannelRequest;
import com.reviewping.coflo.domain.webhookchannel.controller.dto.response.WebhookChannelResponse;
import com.reviewping.coflo.domain.webhookchannel.service.WebhookChannelService;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class WebhookChannelController {

    private final WebhookChannelService webhookChannelService;

    @PostMapping
    public ApiResponse<Void> addWebhookChannel(
            @RequestBody WebhookChannelRequest webhookChannelRequest) {
        webhookChannelService.addWebhookChannel(
                webhookChannelRequest.projectId(),
                webhookChannelRequest.channelCodeId(),
                webhookChannelRequest.webhookUrl());

        return ApiSuccessResponse.success();
    }

    @GetMapping("/{projectId}")
    public ApiResponse<WebhookChannelResponse> getWebhookChannelList(
            @PathVariable("projectId") Long projectId) {
        return ApiSuccessResponse.success(webhookChannelService.getWebhookChannelList(projectId));
    }
}
