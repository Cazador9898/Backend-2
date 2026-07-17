package com.minimarket.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI minimarketOpenAPI() {
        String scheme = "bearerAuth";
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(scheme,
                        new SecurityScheme().name(scheme).type(SecurityScheme.Type.HTTP)
                                .scheme("bearer").bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(scheme))
                .info(new Info()
                        .title("Minimarket Plus - Microservicio principal")
                        .version("3.0.0")
                        .description("API REST segura con JWT, autorización por roles, OpenAPI y HATEOAS.")
                        .contact(new Contact().name("Equipo Minimarket Plus")));
    }
}
