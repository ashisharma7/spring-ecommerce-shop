package com.shop.catalog.web.controller;

import com.shop.catalog.service.CatalogQueryService;
import com.shop.catalog.web.dto.CatalogRequest;
import com.shop.catalog.web.dto.CatalogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("internal/catalog")
@RequiredArgsConstructor
public class InternalCatalogController {

    private final CatalogQueryService catalogQueryService;

    @PostMapping("products/info")
    public CatalogResponse getProductsInfo(@RequestBody CatalogRequest request){
        return catalogQueryService.getProductsInfo(request);
    }

}
