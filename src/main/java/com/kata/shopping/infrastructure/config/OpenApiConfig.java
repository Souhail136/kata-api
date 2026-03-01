package com.kata.shopping.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI shoppingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Kata for Shopping API")
                        .description("Microservice de gestion de produits et de calcul de facture panier")
                        .version("v1.0.0"));
    }
}
