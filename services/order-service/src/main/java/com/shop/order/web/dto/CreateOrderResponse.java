package com.shop.order.web.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateOrderResponse(
        String orderId,
        String orderNumber,
        String status,
        BigDecimal totalAmount,
        Instant createdAt
) {}
