package com.reviewping.coflo.domain.webhookchannel.controller.dto.response;

import com.reviewping.coflo.domain.webhookchannel.entity.WebhookChannel;

public record WebhookChannelResponse(Long webhookChannelId, String channelName, String webhookUrl) {
    public static WebhookChannelResponse of(WebhookChannel webhookChannel) {
        return new WebhookChannelResponse(
                webhookChannel.getId(),
                webhookChannel.getChannelCode().getName().name(),
                webhookChannel.getWebhookUrl());
    }
}
