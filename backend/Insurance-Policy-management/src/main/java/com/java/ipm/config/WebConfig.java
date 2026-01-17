package com.java.ipm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // Allow your React frontend origin
                        .allowedOrigins("http://localhost:3000")

                        // Support all common HTTP methods
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")

                        // Allow all headers (not just Authorization & Content-Type)
                        .allowedHeaders("*")

                        // Expose useful headers to frontend (Authorization, Content-Type, etc.)
                        .exposedHeaders("Authorization", "Content-Type")

                        // Allow sending cookies / Authorization headers
                        .allowCredentials(true)

                        // Cache CORS preflight response for 1 hour (optional)
                        .maxAge(3600);
            }
        };
    }
}
