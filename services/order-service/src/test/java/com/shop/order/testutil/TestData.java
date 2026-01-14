package com.shop.order.testutil;

import com.shop.order.catalog.dto.CatalogResponse;
import com.shop.order.domain.event.OrderCancelledEvent;
import com.shop.order.domain.event.OrderCreatedEvent;
import com.shop.order.domain.model.DeliveryAddress;
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
                createValidCreateOrderRequestDeliveryAddress(),
                List.of(createValidCreateOrderItemRequest())
        );
    }

    public static CreateOrderRequest createInvalidCreateOrderRequest_BlankProductID() {
        return new CreateOrderRequest(
                "user-1",
                createValidCreateOrderRequestDeliveryAddress(),
                List.of(createInvalidCreateOrderItemRequest_BlankID())
        );
    }

    public static CreateOrderRequest createInvalidCreateOrderRequest_BlankUserID() {
        return new CreateOrderRequest(
                " ",
                createValidCreateOrderRequestDeliveryAddress(),
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
        return OrderItem.builder()
                .id(UUID.randomUUID())
                .order(null)
                .productId("pid-random")
                .productName("name-random")
                .price(BigDecimal.valueOf(100))
                .quantity(10)
                .build();
    }

    public static List<OrderItem> createOrderItems() {
        return IntStream.range(1, 6)
                .mapToObj(i -> OrderItem.builder()
                        .id(UUID.randomUUID())
                        .order(null)
                        .productId("pid-" + i)
                        .productName("name-" + i)
                        .price(BigDecimal.valueOf(100))
                        .quantity(i)
                        .build())
                .toList();
    }

    public static Order createNotSavedOrderWithoutItems(long orderNumber) {
        return Order.create("user-1", orderNumber, createValidDeliveryAddress());
    }

    public static Order createNotSavedOrderWithItems(long orderNumber) {
        return Order.create("user-1", orderNumber, createValidDeliveryAddress(), createOrderItems());
    }

    public static Order createSavedOrderWithItems() {
        Order savedOrder = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-" + getRandomNumber())
                .userId("user-1")
                .status(OrderStatus.CREATED)
                .totalAmount(BigDecimal.ZERO)
                .deliveryAddress(createValidDeliveryAddress())
                .createdAt(Instant.now())
                .orderItems(new ArrayList<>())
                .build();
        createOrderItems().forEach(savedOrder::addItem);
        return savedOrder;
    }

    public static Order createSavedOrderWithItems(long orderNumber) {
        Order savedOrder = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-" + orderNumber)
                .userId("user-1")
                .status(OrderStatus.CREATED)
                .totalAmount(BigDecimal.ZERO)
                .deliveryAddress(createValidDeliveryAddress())
                .createdAt(Instant.now())
                .orderItems(new ArrayList<>())
                .build();
        createOrderItems().forEach(savedOrder::addItem);
        return savedOrder;
    }

    public static Order createCancelledOrderWithItems(long orderNumber) {
        Order cancelledOrder = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-" + orderNumber)
                .userId("user-1")
                .status(OrderStatus.CANCELLED)
                .totalAmount(BigDecimal.ZERO)
                .deliveryAddress(createValidDeliveryAddress())
                .createdAt(Instant.now())
                .orderItems(new ArrayList<>())
                .build();
        createOrderItems().forEach(cancelledOrder::addItem);
        return cancelledOrder;
    }

    public static OrderCreatedEvent getOrderCreatedEvent() {
        return new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                BigDecimal.valueOf(1000),
                createValidOrderCreatedEventDeliveryAddress(),
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

    // ===== CreateOrderRequest.DeliveryAddress Test Data =====

    public static CreateOrderRequest.DeliveryAddress createValidCreateOrderRequestDeliveryAddress() {
        return new CreateOrderRequest.DeliveryAddress(
                "Ashish Sharma",
                "+91-9876543210",
                "123 Main Street",
                "Apt 4B",
                "Mumbai",
                "Maharashtra",
                "400001",
                "India"
        );
    }

    // ===== DeliveryAddress Test Data =====

    public static DeliveryAddress createValidDeliveryAddress() {
        return DeliveryAddress.create(
                "Ashish Sharma",
                "+91-9876543210",
                "123 Main Street",
                "Apt 4B",
                "Mumbai",
                "Maharashtra",
                "400001",
                "India"
        );
    }

    public static DeliveryAddress createValidDeliveryAddressWithoutLine2() {
        return DeliveryAddress.builder()
                .fullName("Ashish Sharma")
                .phone("+91-9876543210")
                .line1("123 Main Street")
                .line2(null)
                .city("Mumbai")
                .state("Maharashtra")
                .pinCode("400001")
                .country("India")
                .build();
    }

    public static DeliveryAddress createInvalidDeliveryAddress_BlankFullName() {
        return DeliveryAddress.builder()
                .fullName(" ")
                .phone("+91-9876543210")
                .line1("123 Main Street")
                .city("Mumbai")
                .state("Maharashtra")
                .pinCode("400001")
                .country("India")
                .build();
    }

    public static DeliveryAddress createInvalidDeliveryAddress_BlankPhone() {
        return DeliveryAddress.builder()
                .fullName("Ashish Sharma")
                .phone(" ")
                .line1("123 Main Street")
                .city("Mumbai")
                .state("Maharashtra")
                .pinCode("400001")
                .country("India")
                .build();
    }

    public static DeliveryAddress createInvalidDeliveryAddress_BlankLine1() {
        return DeliveryAddress.builder()
                .fullName("Ashish Sharma")
                .phone("+91-9876543210")
                .line1(" ")
                .city("Mumbai")
                .state("Maharashtra")
                .pinCode("400001")
                .country("India")
                .build();
    }

    public static DeliveryAddress createInvalidDeliveryAddress_BlankCity() {
        return DeliveryAddress.builder()
                .fullName("Ashish Sharma")
                .phone("+91-9876543210")
                .line1("123 Main Street")
                .city(" ")
                .state("Maharashtra")
                .pinCode("400001")
                .country("India")
                .build();
    }

    public static DeliveryAddress createInvalidDeliveryAddress_BlankState() {
        return DeliveryAddress.builder()
                .fullName("Ashish Sharma")
                .phone("+91-9876543210")
                .line1("123 Main Street")
                .city("Mumbai")
                .state(" ")
                .pinCode("400001")
                .country("India")
                .build();
    }

    public static DeliveryAddress createInvalidDeliveryAddress_BlankPinCode() {
        return DeliveryAddress.builder()
                .fullName("Ashish Sharma")
                .phone("+91-9876543210")
                .line1("123 Main Street")
                .city("Mumbai")
                .state("Maharashtra")
                .pinCode(" ")
                .country("India")
                .build();
    }

    public static DeliveryAddress createInvalidDeliveryAddress_BlankCountry() {
        return DeliveryAddress.builder()
                .fullName("Ashish Sharma")
                .phone("+91-9876543210")
                .line1("123 Main Street")
                .city("Mumbai")
                .state("Maharashtra")
                .pinCode("400001")
                .country(" ")
                .build();
    }

    // ===== OrderCreatedEvent.DeliveryAddress Test Data =====

    public static OrderCreatedEvent.DeliveryAddress createValidOrderCreatedEventDeliveryAddress() {
        return new OrderCreatedEvent.DeliveryAddress(
                "Ashish Sharma",
                "+91-9876543210",
                "123 Main Street",
                "Apt 4B",
                "Mumbai",
                "Maharashtra",
                "400001",
                "India"
        );
    }
}
