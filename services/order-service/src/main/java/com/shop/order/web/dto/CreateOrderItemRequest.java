package com.shop.order.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateOrderItemRequest(
        @NotBlank(message = "Product Id must not be blank")
        String productId,
        @Min(value = 1, message = "Quantity must be equal or greater than 1")
        Integer quantity
) {}
