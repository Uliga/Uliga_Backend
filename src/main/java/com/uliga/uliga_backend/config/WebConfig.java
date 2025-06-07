package com.uliga.uliga_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebConfig implements WebFluxConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOriginPatterns("https://www.ouruliga.com/")
        .allowedMethods("GET", "POST", "PATCH", "DELETE", "HEAD", "OPTIONS")
        .allowCredentials(true)
        .maxAge(3000);
  }
}
