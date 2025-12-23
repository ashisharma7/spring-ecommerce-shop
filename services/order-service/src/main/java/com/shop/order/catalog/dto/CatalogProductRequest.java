package com.shop.order.catalog.dto;

public record CatalogProductRequest(
        String productId,
        int quantity
) {}
