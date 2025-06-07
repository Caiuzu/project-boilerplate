package com.app.core.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulith;

@Modulith
@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "App Core API",
		version = "1.0.0",
		description = "API para sistema de pedidos de fast app, seguindo arquitetura hexagonal, DDD e design modular",
		contact = @Contact(
			name = "Equipe FIAP/SOAT",
			email = "suporte@appcoreapi.com",
			url = "https://github.com/fiap-soat/app-core-api"
		),
		license = @License(
			name = "Apache 2.0",
			url = "https://www.apache.org/licenses/LICENSE-2.0.html"
		)
	),
	servers = {
		@Server(url = "/api", description = "API Server")
	}
)
public class AppCoreApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppCoreApiApplication.class, args);
	}
}
