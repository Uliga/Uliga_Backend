package com.uliga.uliga_backend.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "<우리가>",
                description = "공유 가계부 <우리가> API 명세서",
                version = "1.0",
                contact = @Contact(name = "<우리가> 개발팀", url = "https://github.com/Uliga", email = "uliga_dev_team@naver.com")
        )

)
@RequiredArgsConstructor
@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi uligaApi() {
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .displayName("API Doc Ver 1.0")
                .group("Uliga_v1")
                .pathsToMatch(paths)
                .build();
    }

}
