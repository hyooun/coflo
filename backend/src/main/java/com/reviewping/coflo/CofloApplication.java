package com.reviewping.coflo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@IntegrationComponentScan
public class CofloApplication {

    public static void main(String[] args) {
        SpringApplication.run(CofloApplication.class, args);
    }
}
