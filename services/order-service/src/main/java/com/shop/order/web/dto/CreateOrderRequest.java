package com.shop.order.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @NotBlank(message = "User Id must not be blank")
        String userId,
        @NotEmpty(message = "Item List must not be empty")
        @Valid //ensures nested validation
        List<CreateOrderItemRequest> orderItems
) {}
