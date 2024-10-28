package com.reviewping.coflo.domain.webhookchannel.controller.dto.request;

public record WebhookChannelRequest(Long projectId, Long channelCodeId, String webhookUrl) {}
