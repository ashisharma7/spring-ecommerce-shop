package com.shop.order.testutil;

import com.shop.order.catalog.dto.CatalogResponse;
import com.shop.order.domain.event.OrderCancelledEvent;
import com.shop.order.domain.event.OrderCreatedEvent;
import com.shop.order.domain.model.Order;
import com.shop.order.domain.model.OrderItem;
import com.shop.order.domain.model.OrderStatus;
import com.shop.order.web.dto.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

public final class TestData {
    private TestData() {
    }

    public static long getRandomNumber() {
        return new Random().nextLong();
    }

    public static CreateOrderItemRequest createValidCreateOrderItemRequest() {
        return new CreateOrderItemRequest("pid-1", 1);
    }

    public static CreateOrderItemRequest createInvalidCreateOrderItemRequest_BlankID() {
        return new CreateOrderItemRequest(" ", 1);
    }

    public static CreateOrderRequest createValidCreateOrderRequest() {
        return new CreateOrderRequest(
                "user-1",
                List.of(createValidCreateOrderItemRequest())
        );
    }

    public static CreateOrderRequest createInvalidCreateOrderRequest_BlankProductID() {
        return new CreateOrderRequest(
                "user-1",
                List.of(createInvalidCreateOrderItemRequest_BlankID())
        );
    }

    public static CreateOrderRequest createInvalidCreateOrderRequest_BlankUserID() {
        return new CreateOrderRequest(
                " ",
                List.of(createInvalidCreateOrderItemRequest_BlankID())
        );
    }

    public static CreateOrderResponse createValidCreateOrderResponse() {
        return new CreateOrderResponse(
                UUID.randomUUID().toString(),
                "ORD-1",
                "CREATED",
                new BigDecimal("199.99"),
                Instant.now()
        );
    }

    public static OrderResponse createValidOrderResponse(){
        return new OrderResponse(
                UUID.randomUUID().toString(),
                "ORD-1",
                "user-1",
                "CREATED",
                new BigDecimal("100"),
                Instant.now(),
                List.of(createValidOrderItemResponse())
        );
    }

    public static OrderItemResponse createValidOrderItemResponse(){
        return new OrderItemResponse(
                "pid-1",
                "name-1",
                BigDecimal.valueOf(100),
                1
        );
    }

    public static CatalogResponse.CatalogProductResponse createValidCatalogProduct() {
        return new CatalogResponse.CatalogProductResponse(
                "pid-1",
                "name-1",
                BigDecimal.valueOf(100),
                Boolean.TRUE);
    }

    public static CatalogResponse.CatalogProductResponse createInvalidCatalogProduct_NotAvailable() {
        return new CatalogResponse.CatalogProductResponse(
                "pid-1",
                "name-1",
                BigDecimal.valueOf(100),
                Boolean.FALSE);
    }

    public static CatalogResponse.CatalogProductResponse createInvalidCatalogProduct_NegativePrice() {
        return new CatalogResponse.CatalogProductResponse(
                "pid-1",
                "name-1",
                BigDecimal.valueOf(-100),
                Boolean.TRUE);
    }

    public static CatalogResponse.CatalogProductResponse createInvalidCatalogProduct_BlankID() {
        return new CatalogResponse.CatalogProductResponse(
                " ",
                "name-1",
                BigDecimal.valueOf(100),
                Boolean.TRUE);
    }

    public static OrderItem createOrderItem() {
        return new OrderItem(
                UUID.randomUUID(),
                null,
                "pid-random",
                "name-random",
                BigDecimal.valueOf(100),
                10
        );
    }

    public static List<OrderItem> createOrderItems() {
        return IntStream.range(1, 6)
                .mapToObj(i -> {
                    return new OrderItem(
                            UUID.randomUUID(),
                            null,
                            "pid-" + i,
                            "name-" + i,
                            BigDecimal.valueOf(100),
                            i
                    );
                })
                .toList();
    }

    public static Order createNotSavedOrderWithoutItems(long orderNumber) {
        return Order.create("user-1", orderNumber);
    }

    public static Order createNotSavedOrderWithItems(long orderNumber) {
        return Order.create("user-1", orderNumber, createOrderItems());
    }

    public static Order createSavedOrderWithItems() {
        Order savedOrder = new Order(
                UUID.randomUUID(),
                "ORD-" + getRandomNumber(),
                "user-1",
                OrderStatus.CREATED,
                BigDecimal.ZERO,
                Instant.now(),
                new ArrayList<>()
        );
        createOrderItems().forEach(savedOrder::addItem);
        return savedOrder;
    }

    public static Order createSavedOrderWithItems(long orderNumber) {
        Order savedOrder = new Order(
                UUID.randomUUID(),
                "ORD-" + orderNumber,
                "user-1",
                OrderStatus.CREATED,
                BigDecimal.ZERO,
                Instant.now(),
                new ArrayList<>()
        );
        createOrderItems().forEach(savedOrder::addItem);
        return savedOrder;
    }

    public static Order createCancelledOrderWithItems(long orderNumber) {
        Order savedOrder = new Order(
                UUID.randomUUID(),
                "ORD-" + orderNumber,
                "user-1",
                OrderStatus.CANCELLED,
                BigDecimal.ZERO,
                Instant.now(),
                new ArrayList<>()
        );
        createOrderItems().forEach(savedOrder::addItem);
        return savedOrder;
    }

    public static OrderCreatedEvent getOrderCreatedEvent() {
        return new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                BigDecimal.valueOf(1000),
                List.of(new OrderCreatedEvent.OrderItemEvent("pid-1", 1))
        );
    }

    public static OrderCancelledEvent getOrderCancelledEvent() {
        return new OrderCancelledEvent(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "Cancel my order",
                Instant.now()
        );
    }

    public static CancelOrderRequest createValidCancelOrderRequest(){
        return new CancelOrderRequest(
                UUID.randomUUID().toString(),
                "user-1",
                "Cancel my order"
        );
    }

    public static CancelOrderRequest createInvalidCancelOrderRequest_WrongUserID(){
        return new CancelOrderRequest(
                UUID.randomUUID().toString(),
                "user-wrong",
                "Cancel my order"
        );
    }

    public static CancelOrderResponse createValidCancelOrderResponse(){
        return new CancelOrderResponse(
                UUID.randomUUID().toString(),
                "CANCELLED",
                "Cancel my order",
                Instant.now()
        );
    }

}
