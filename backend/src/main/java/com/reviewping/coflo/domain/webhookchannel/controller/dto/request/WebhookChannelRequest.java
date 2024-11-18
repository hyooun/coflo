package com.reviewping.coflo.domain.webhookchannel.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WebhookChannelRequest(
        @NotNull Long projectId, @NotNull Long channelCodeId, @NotBlank String webhookUrl) {}
