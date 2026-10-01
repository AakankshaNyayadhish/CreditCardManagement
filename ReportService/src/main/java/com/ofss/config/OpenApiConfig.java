package com.ofss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI applicationOpenAPI() {

        String securitySchemeName = "bearerAuth";

        Info apiInfo = new Info();
        apiInfo.setTitle("Credit Card Management API");
        apiInfo.setVersion("1.0");
        apiInfo.setDescription(
                "REST API for credit card management"
        );

        SecurityScheme bearerScheme = new SecurityScheme();
        bearerScheme.setName("Authorization");
        bearerScheme.setType(SecurityScheme.Type.HTTP);
        bearerScheme.setScheme("bearer");
        bearerScheme.setBearerFormat("JWT");

        Components components = new Components();
        components.addSecuritySchemes(
                securitySchemeName,
                bearerScheme
        );

        SecurityRequirement securityRequirement =
                new SecurityRequirement()
                        .addList(securitySchemeName);

        return new OpenAPI()
                .info(apiInfo)
                .components(components)
                .addSecurityItem(securityRequirement);
    }
}