package com.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        System.out.println(">>> STARTING");
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        System.out.println(">>> APPLICATION READY");
    }

    @Bean
    public ApplicationListener<ApplicationFailedEvent> failedEventApplicationListener() {
        return event -> {
            System.err.println(">>> APPLICATION FAILED TO START");
            event.getException().printStackTrace();
        };
    }
}