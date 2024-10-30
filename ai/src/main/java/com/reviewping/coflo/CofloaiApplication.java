package com.reviewping.coflo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan
public class CofloaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CofloaiApplication.class, args);
    }
}
