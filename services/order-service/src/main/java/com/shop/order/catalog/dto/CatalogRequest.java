package com.shop.order.catalog.dto;

import java.util.List;

public record CatalogRequest(List<CatalogProductRequest> items) {
    public record CatalogProductRequest(
            String productId,
            int quantity
    ) {}
}