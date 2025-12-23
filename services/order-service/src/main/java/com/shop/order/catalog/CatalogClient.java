package com.shop.order.catalog;

import com.shop.order.catalog.dto.CatalogProductRequest;
import com.shop.order.catalog.dto.CatalogProductResponse;

import java.util.List;

public interface CatalogClient {
    List<CatalogProductResponse> fetchProducts(List<CatalogProductRequest> items);
}
