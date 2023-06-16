package com.uliga.uliga_backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("https://www.ouruliga.com/")
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "HEAD", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3000);
    }
}
