package com.shop.order.web.mapper;

import com.shop.order.catalog.dto.CatalogProductResponse;
import com.shop.order.domain.model.Order;
import com.shop.order.domain.model.OrderItem;
import com.shop.order.web.dto.CreateOrderItemRequest;
import com.shop.order.web.dto.CreateOrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    CreateOrderResponse toCreateOrderResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "quantity", source = "request.quantity")
    OrderItem toOrderItem(CreateOrderItemRequest request, CatalogProductResponse product);
}