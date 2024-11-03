package com.reviewping.coflo.global.integration;

import com.reviewping.coflo.domain.review.message.ReviewRequest;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "redisOutboundChannel")
public interface RedisGateway {

    @Gateway(headers = @GatewayHeader(name = "topic", value = "init"))
    void sendInitRequest(String message);

    @Gateway(headers = @GatewayHeader(name = "topic", value = "review-request"))
    void sendReviewRequest(ReviewRequest reviewRequest);

    @Gateway(headers = @GatewayHeader(name = "topic", value = "mr-eval-request"))
    void sendEvalRequest(String message);

    @Gateway(headers = @GatewayHeader(name = "topic", value = "test"))
    void sendTest(String message);
}
