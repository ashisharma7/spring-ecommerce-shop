package com.shop.order.web.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        String productId,
        String productName,
        BigDecimal price,
        int quantity
) {}