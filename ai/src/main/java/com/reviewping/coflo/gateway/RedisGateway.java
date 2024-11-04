package com.reviewping.coflo.gateway;

import com.reviewping.coflo.service.dto.response.ReviewResponseMessage;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "redisOutboundChannel")
public interface RedisGateway {

    @Gateway(headers = @GatewayHeader(name = "topic", value = "review-response"))
    void sendReview(ReviewResponseMessage reviewResponse);

    @Gateway(headers = @GatewayHeader(name = "topic", value = "mr-eval-response"))
    void sendEval(String message);

    @Gateway(headers = @GatewayHeader(name = "topic", value = "test"))
    void sendTest(String message);
}
