package com.reviewping.coflo.domain.webhookchannel.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateWebhookChannelRequest(@NotBlank String webhookUrl) {}
