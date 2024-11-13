package com.reviewping.coflo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.redis.inbound.RedisInboundChannelAdapter;
import org.springframework.integration.redis.outbound.RedisPublishingMessageHandler;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Slf4j
@Configuration
@RequiredArgsConstructor
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
    public MessageChannel updateChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel reviewRequestChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel mrEvalRequestChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel reviewRegenerateRequestChannel() {
        return new DirectChannel();
    }

    @Bean
    public RedisInboundChannelAdapter redisInboundChannelAdapter(
            @Qualifier("redisInboundChannel") MessageChannel redisInboundChannel) {

        RedisInboundChannelAdapter adapter = new RedisInboundChannelAdapter(redisConnectionFactory);
        adapter.setTopics(
                "test", "update", "review-request", "mr-eval-request", "review-regenerate-request");
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
        Jackson2JsonRedisSerializer<Object> serializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        handler.setSerializer(serializer);
        return handler;
    }

    @ServiceActivator(inputChannel = "redisInboundChannel")
    @Bean
    public HeaderValueRouter router() {
        HeaderValueRouter router = new HeaderValueRouter("redis_messageSource");
        router.setChannelMapping("test", "testInboundChannel");
        router.setChannelMapping("update", "updateChannel");
        router.setChannelMapping("review-request", "reviewRequestChannel");
        router.setChannelMapping("mr-eval-request", "mrEvalRequestChannel");
        router.setChannelMapping("review-regenerate-request", "reviewRegenerateRequestChannel");
        return router;
    }
}
