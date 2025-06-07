package com.app.core.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do SpringDoc para documentação da API
 */
@Configuration
public class SpringDocConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("App Core API")
                        .description("API para sistema de pedidos de fast app com arquitetura hexagonal")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe FIAP/SOAT")
                                .email("suporte@appcoreapi.com")
                                .url("https://github.com/fiap-soat/app-core-api"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url(contextPath)
                                .description("API Server")
                ));
    }

    @Bean
    public GroupedOpenApi catalogApi() {
        return GroupedOpenApi.builder()
                .group("catálogos")
                .packagesToScan("com.app.core.api.catalog")
                .pathsToMatch("/catalogs/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("usuários")
                .packagesToScan("com.app.core.api.user")
                .pathsToMatch("/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("pedidos")
                .packagesToScan("com.app.core.api.order")
                .pathsToMatch("/orders/**")
                .build();
    }

    @Bean
    public GroupedOpenApi paymentApi() {
        return GroupedOpenApi.builder()
                .group("pagamentos")
                .packagesToScan("com.app.core.api.payment")
                .pathsToMatch("/payments/**")
                .build();
    }
} 