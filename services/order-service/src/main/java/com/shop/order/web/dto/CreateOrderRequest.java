package com.shop.order.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotBlank(message = "User Id must not be blank")
        String userId,
        @NotNull(message = "Delivery address must be present")
        @Valid
        DeliveryAddress deliveryAddress,
        @NotEmpty(message = "Item List must not be empty")
        @Valid //ensures nested validation
        List<CreateOrderItemRequest> orderItems
) {
        public record DeliveryAddress(
                @NotBlank(message = "Full name must not be blank")
                String fullName,
                @NotBlank(message = "Phone must not be blank")
                String phone,
                @NotBlank(message = "Line 1 must not be blank")
                String line1,
                String line2,
                @NotBlank(message = "City must not be blank")
                String city,
                @NotBlank(message = "State must not be blank")
                String state,
                @NotBlank(message = "Pin code must not be blank")
                String pinCode,
                @NotBlank(message = "Country must not be blank")
                String country
        ){}
}
