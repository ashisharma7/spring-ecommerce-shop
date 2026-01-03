package com.shop.order.catalog;

import com.shop.order.catalog.dto.CatalogRequest;
import com.shop.order.catalog.dto.CatalogResponse;

public interface CatalogClient {
    CatalogResponse fetchProducts(CatalogRequest catalogRequest);
}
