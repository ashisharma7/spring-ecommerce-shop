package com.shop.catalog.web.mapper;

import com.shop.catalog.domain.model.Product;
import com.shop.catalog.web.dto.CatalogResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "productId", source = "id")
    CatalogResponse.CatalogProductResponse toCatalogProductResponse(Product product);

}