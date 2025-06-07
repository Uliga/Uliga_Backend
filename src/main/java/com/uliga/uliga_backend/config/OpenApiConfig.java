package com.uliga.uliga_backend.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;

@OpenAPIDefinition(info = @Info(title = "<우리가>", description = "공유 가계부 <우리가> API 명세서", version = "1.0"), servers = {
    @Server(url = "https://api.ouruliga.com", description = "server")
})
@RequiredArgsConstructor
@Configuration
public class OpenApiConfig {
  @Bean
  public GroupedOpenApi uligaApiV1() {
    return GroupedOpenApi.builder()
        .displayName("API Doc Ver 1.0")
        .group("v1")
        .pathsToMatch("/v1/**")
        .build();
  }

  @Bean
  public GroupedOpenApi uligaApiV2() {
    return GroupedOpenApi.builder()
        .displayName("API Doc Ver 2.0")
        .group("v2")
        .pathsToMatch("/v2/**")
        .build();
  }
}
