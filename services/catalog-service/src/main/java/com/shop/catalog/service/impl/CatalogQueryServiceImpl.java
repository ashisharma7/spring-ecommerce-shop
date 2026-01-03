package com.shop.catalog.service.impl;

import com.shop.catalog.repository.CategoryRepository;
import com.shop.catalog.repository.ProductRepository;
import com.shop.catalog.service.CatalogQueryService;
import com.shop.catalog.web.dto.CatalogRequest;
import com.shop.catalog.web.dto.CatalogResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogQueryServiceImpl implements CatalogQueryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public CatalogResponse getProductsInfo(CatalogRequest request) {
        return null;
    }

}
