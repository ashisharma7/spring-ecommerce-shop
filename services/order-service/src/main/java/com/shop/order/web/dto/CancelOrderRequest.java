package com.shop.order.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CancelOrderRequest(
        @NotBlank(message = "Order ID is required")
        String orderId,

        @NotBlank(message = "User ID is required")
        String userId,

        @NotBlank(message = "Cancellation reason is required")
        String reason
) {}