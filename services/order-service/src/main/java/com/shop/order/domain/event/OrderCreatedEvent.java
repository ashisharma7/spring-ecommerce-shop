package com.shop.order.domain.event;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreatedEvent(
        String orderId,
        String userId,
        BigDecimal totalAmount,
        List<OrderItemEvent> items
) {
    public record OrderItemEvent(
            String productId,
            int quantity
    ) {}
}
