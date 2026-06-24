package com.carvalx.ascua_burger.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Le dice a Swagger que existe un esquema de seguridad llamado "bearerAuth"
// que usa JWT en la cabecera Authorization: Bearer <token>
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ascua Burger API")
                        .description("API REST para hamburguesería con JWT, paginación y recomendador IA")
                        .version("1.0.0"));
    }
}