package com.shop.order.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Order Service API",
        description = "APIs for order creation and lifecycle management",
        version = "v1"
    )
)
public class OpenApiConfig {
}
