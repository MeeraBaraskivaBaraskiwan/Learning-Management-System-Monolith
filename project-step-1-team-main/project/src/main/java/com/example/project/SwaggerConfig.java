package com.example.project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            // Specify that our endpoints use Bearer Authentication (JWT)
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components().addSecuritySchemes("Bearer Authentication", createBearerScheme()))
            // Set API metadata. Adjust the contact info as needed.
            .info(new Info()
                .title("Ctrl+Learn LMS API")
                .version("1.0")
                .description("Monolithic LMS API Documentation with JWT Security and Email Notifications")
                .contact(new Contact()
                    .name("Ctrl+Learn Support")
                    .email("meera.paraskiva@gmail.com")
                    .url("N/A"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://www.apache.org/licenses/LICENSE-2.0")));
    }

    private SecurityScheme createBearerScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");
    }
}