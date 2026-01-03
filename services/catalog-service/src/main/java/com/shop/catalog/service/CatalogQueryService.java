package com.shop.catalog.service;

import com.shop.catalog.web.dto.CatalogRequest;
import com.shop.catalog.web.dto.CatalogResponse;

public interface CatalogQueryService {
    CatalogResponse getProductsInfo(CatalogRequest request);
}
