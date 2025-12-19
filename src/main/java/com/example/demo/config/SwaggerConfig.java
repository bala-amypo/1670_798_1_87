package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port}")
    private String port;

    @Bean
    public OpenAPI customOpenAPI() {

        Server server = new Server();
        server.setUrl("https://9103.408procr.amypo.ai/" + port);
        server.setDescription("Local Server");

        return new OpenAPI()
                .servers(List.of(server));
    }
}
