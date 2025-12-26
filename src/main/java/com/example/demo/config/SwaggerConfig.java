package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()

                // ✅ KEEP YOUR SERVER (unchanged)
                .servers(List.of(
                        new Server().url("https://9195.pro604cr.amypo.ai/")
                ))

                // ✅ ADD SECURITY REQUIREMENT (this triggers Authorize button)
                .addSecurityItem(
                        new SecurityRequirement().addList("bearerAuth")
                )

                // ✅ DEFINE JWT BEARER SCHEME
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}