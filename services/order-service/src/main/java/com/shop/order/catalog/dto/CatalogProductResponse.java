package com.shop.order.catalog.dto;

import java.math.BigDecimal;

public record CatalogProductResponse(
        String productId,
        String name,
        BigDecimal price,
        Boolean available
) {}
