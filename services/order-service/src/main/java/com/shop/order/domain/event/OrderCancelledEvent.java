package com.shop.order.domain.event;

import java.time.Instant;

public record OrderCancelledEvent(
        String orderId,
        String userId,
        String reason,
        Instant cancelledAt
) {}