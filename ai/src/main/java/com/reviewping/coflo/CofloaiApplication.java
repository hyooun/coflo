package com.reviewping.coflo;

import java.io.IOException;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan
public class CofloaiApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(CofloaiApplication.class, args);
    }

    // 애플리케이션 종료 방지
    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            Thread.currentThread().join();
        };
    }
}
