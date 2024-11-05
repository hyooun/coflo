package com.reviewping.coflo.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        servers = {
            @Server(url = "http://localhost:8080"),
            @Server(url = "https://www.coflo.co.kr"),
            @Server(url = "https://k11a210.p.ssafy.io")
        })
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().version("v1.0").title("Coflo Rest API").description("coflo"));
    }

    @Bean
    GroupedOpenApi allApi() {
        return GroupedOpenApi.builder().group("all").pathsToMatch("/api/**").build();
    }

    @Bean
    GroupedOpenApi userApi() {
        return GroupedOpenApi.builder().group("user").pathsToMatch("/api/users/**").build();
    }

    @Bean
    GroupedOpenApi customPromptApi() {
        return GroupedOpenApi.builder()
                .group("custom-prompt")
                .pathsToMatch("/api/custom-prompts/**")
                .build();
    }

    @Bean
    GroupedOpenApi userProjectApi() {
        return GroupedOpenApi.builder()
                .group("user-project")
                .pathsToMatch("/api/user-project/**")
                .build();
    }

    @Bean
    GroupedOpenApi channelApi() {
        return GroupedOpenApi.builder()
                .group("webhook-channel")
                .pathsToMatch("/api/channels/**")
                .build();
    }

    @Bean
    GroupedOpenApi projectApi() {
        return GroupedOpenApi.builder().group("project").pathsToMatch("/api/projects/**").build();
    }

    @Bean
    GroupedOpenApi mergeRequestApi() {
        return GroupedOpenApi.builder()
                .group("merge-request")
                .pathsToMatch("/api/merge-requests/**")
                .build();
    }

    @Bean
    GroupedOpenApi gitlabApi() {
        return GroupedOpenApi.builder().group("gitlab").pathsToMatch("/api/gitlab/**").build();
    }
}
