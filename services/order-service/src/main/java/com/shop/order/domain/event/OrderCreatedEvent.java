package com.shop.order.domain.event;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreatedEvent(
        String orderId,
        String userId,
        BigDecimal totalAmount,
        DeliveryAddress deliveryAddress,
        List<OrderItemEvent> items
) {
    public record OrderItemEvent(
            String productId,
            int quantity
    ) {}
    public record DeliveryAddress(
            String fullName,
            String phone,
            String line1,
            String line2,
            String city,
            String state,
            String pinCode,
            String country
    ) {}
}
