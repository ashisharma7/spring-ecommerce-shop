package com.shop.order.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CreateOrderResponse(
        UUID orderId,
        String orderNumber,
        String status,
        BigDecimal totalAmount,
        Instant createdAt
) {}
