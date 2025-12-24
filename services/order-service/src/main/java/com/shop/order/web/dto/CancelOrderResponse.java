package com.shop.order.web.dto;

import java.time.Instant;

public record CancelOrderResponse(
        String orderId,
        String status,
        String reason,
        Instant cancelledAt
) {}