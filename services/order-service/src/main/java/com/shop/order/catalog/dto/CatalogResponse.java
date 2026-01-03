package com.shop.order.catalog.dto;

import java.math.BigDecimal;
import java.util.List;

public record CatalogResponse(List<CatalogProductResponse> products) {
    public record CatalogProductResponse(
            String productId,
            String name,
            BigDecimal price,
            Boolean available
    ) {}
}