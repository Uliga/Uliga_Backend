package com.uliga.uliga_backend.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Uliga",
                description = "공유 가계부 <우리가> API 명세서",
                version = "1.0")
)
@RequiredArgsConstructor
@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi uligaApi() {
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("ULIGA v1")
                .pathsToMatch(paths)
                .build();
    }

}
