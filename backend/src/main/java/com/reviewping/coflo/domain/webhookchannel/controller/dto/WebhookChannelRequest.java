package com.reviewping.coflo.domain.webhookchannel.controller.dto;

public record WebhookChannelRequest(Long projectId, Long channelCodeId, String webhookUrl) {}
