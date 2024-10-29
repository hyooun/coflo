package com.reviewping.coflo.domain.webhookchannel.controller.dto.response;

import java.util.List;

public record WebhookChannelResponse(List<WebhookChannelContent> webhookChannelContentList) {
    public static WebhookChannelResponse of(List<WebhookChannelContent> webhookChannelContentList) {
        return new WebhookChannelResponse(webhookChannelContentList);
    }
}
