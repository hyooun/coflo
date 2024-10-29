package com.reviewping.coflo.domain.webhookchannel.controller.dto.response;

import com.reviewping.coflo.domain.webhookchannel.entity.WebhookChannel;

public record WebhookChannelContent(Long webhookChannelId, String channelName, String webhookUrl) {
    public static WebhookChannelContent of(WebhookChannel webhookChannel) {
        return new WebhookChannelContent(
                webhookChannel.getId(),
                webhookChannel.getChannelCode().getName().name(),
                webhookChannel.getWebhookUrl());
    }
}
