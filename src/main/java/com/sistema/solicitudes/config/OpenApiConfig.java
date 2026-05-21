package com.sistema.solicitudes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/**
 * Configuración de OpenAPI / Swagger para la documentación de la API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Solicitudes de Soporte Técnico")
                        .version("1.0.0")
                        .description("API RESTful para registrar, consultar, actualizar y eliminar "
                                + "solicitudes de soporte técnico. Desarrollada con Spring Boot 4, "
                                + "Java 25, persistencia en memoria y documentación OpenAPI.")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("soporte@empresa.com")));
    }
}
