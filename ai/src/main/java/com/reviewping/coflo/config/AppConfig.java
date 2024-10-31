package com.reviewping.coflo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.treesitter.TSParser;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TSParser tsParser() {
        return new TSParser();
    }
}
