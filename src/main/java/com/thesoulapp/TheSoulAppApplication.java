package com.thesoulapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TheSoulAppApplication {

    @Value("${app.api.base-url:http://localhost:8080/api}")
    private String apiBaseUrl;

    @Value("${cors.allowed-origins:https://thesoulapp.org,http://localhost:3000}")
    private String corsAllowedOrigins;

    @PostConstruct
    public void printBaseUrl() {
        System.out.println("API Base URL: " + apiBaseUrl);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                String[] origins = corsAllowedOrigins.split(",");
                registry.addMapping("/**")
                        .allowedOrigins(origins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("Authorization", "Content-Type")
                        .allowCredentials(true);
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(TheSoulAppApplication.class, args);
    }
} 