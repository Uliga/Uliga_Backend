package com.uliga.uliga_backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://0.0.0.0:3000")
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .allowCredentials(true)
                .maxAge(3000);
    }
}
