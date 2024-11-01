package com.reviewping.coflo;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan
public class CofloaiApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(CofloaiApplication.class, args);
        // for remaining running state
        //noinspection ResultOfMethodCallIgnored
        System.in.read();
    }
}
