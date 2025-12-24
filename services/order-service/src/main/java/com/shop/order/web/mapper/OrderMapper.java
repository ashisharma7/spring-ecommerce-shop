package com.shop.order.web.mapper;

import com.shop.order.catalog.dto.CatalogProductResponse;
import com.shop.order.domain.event.OrderCreatedEvent;
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

    @Mapping(target = "orderId", expression = "java(order.getId().toString())")
    @Mapping(target = "items", source = "orderItems")
    OrderCreatedEvent toOrderCreatedEvent(Order order);

    OrderCreatedEvent.OrderItemEvent toOrderItemEvent(OrderItem orderItem);

    @Mapping(target = "orderId", expression = "java(order.getId().toString())")
    @Mapping(target = "reason", source = "reason")
    @Mapping(target = "cancelledAt", expression = "java(java.time.Instant.now())")
    OrderCancelledEvent toOrderCancelledEvent(Order order, String reason);
}