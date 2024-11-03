package com.reviewping.coflo.global.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.redis.inbound.RedisInboundChannelAdapter;
import org.springframework.integration.redis.outbound.RedisPublishingMessageHandler;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Slf4j
@Configuration
@RequiredArgsConstructor
@IntegrationComponentScan(basePackages = "com.reviewping.coflo")
public class RedisIntegrationConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public MessageChannel redisInboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel redisOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel reviewResponseChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel mrEvalResponseChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel testInboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel testOutboundChannel() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = "testInboundChannel")
    public void handleMessage(Message<?> message) {
        System.out.println("Received test message from Redis: " + message);
    }

    @Bean
    public RedisInboundChannelAdapter redisInboundChannelAdapter(
            MessageChannel redisInboundChannel) {
        RedisInboundChannelAdapter adapter = new RedisInboundChannelAdapter(redisConnectionFactory);
        adapter.setTopics("test", "init", "review-response", "mr-eval-response");
        adapter.setOutputChannel(redisInboundChannel);
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "redisOutboundChannel")
    public MessageHandler redisOutboundAdapter() {
        RedisPublishingMessageHandler handler =
                new RedisPublishingMessageHandler(redisConnectionFactory);
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression topicExpression = parser.parseExpression("headers['topic']");
        handler.setTopicExpression(topicExpression);
        return handler;
    }

    @ServiceActivator(inputChannel = "redisInboundChannel")
    @Bean
    public HeaderValueRouter router() {
        HeaderValueRouter router = new HeaderValueRouter("redis_messageSource");
        router.setChannelMapping("test", "testInboundChannel");
        router.setChannelMapping("review-response", "reviewResponseChannel");
        router.setChannelMapping("mr-eval-response", "mrEvalResponseChannel");
        return router;
    }
}
