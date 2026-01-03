package com.shop.catalog.web.dto;

import java.util.List;

public record CatalogRequest(List<CatalogProductRequest> items) {
    public record CatalogProductRequest(
            String productId,
            int quantity
    ) {}
}