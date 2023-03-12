package com.uliga.uliga_backend.global.component;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OpenApiComponent {
    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement requirement = new SecurityRequirement().addList("bearerAuth");
        return new OpenAPI().components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(requirement));
    }
}
