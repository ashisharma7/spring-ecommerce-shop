package com.shop.catalog.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Catalog Service API",
        description = "APIs for catalog products extraction, creation and lifecycle management",
        version = "v1"
    )
)
public class OpenApiConfig {
}
