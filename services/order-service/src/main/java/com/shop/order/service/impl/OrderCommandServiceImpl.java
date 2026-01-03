package com.shop.order.service.impl;

import com.shop.order.catalog.CatalogClient;
import com.shop.order.catalog.dto.CatalogRequest;
import com.shop.order.catalog.dto.CatalogResponse;
import com.shop.order.catalog.exception.ProductNotFoundException;
import com.shop.order.domain.event.OrderCancelledEvent;
import com.shop.order.domain.event.OrderCreatedEvent;
import com.shop.order.domain.event.OrderEventPublisher;
import com.shop.order.domain.exception.InvalidOrderStateException;
import com.shop.order.domain.exception.OrderNotFoundException;
import com.shop.order.domain.model.Order;
import com.shop.order.domain.model.OrderItem;
import com.shop.order.repository.OrderRepository;
import com.shop.order.service.OrderCommandService;
import com.shop.order.web.dto.*;
import com.shop.order.web.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements OrderCommandService {
    private final OrderRepository orderRepository;
    private final CatalogClient catalogClient;
    private final OrderMapper orderMapper;
    private final OrderEventPublisher orderEventPublisher;

    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) {
        log.info("Processing create order request for user: {}", createOrderRequest.userId());
        Map<String, CatalogResponse.CatalogProductResponse> catalogProductDataMap = fetchCatalogData(createOrderRequest);
        List<OrderItem> orderItems = buildOrderItems(createOrderRequest, catalogProductDataMap);
        Long nextOrderNumber = orderRepository.getNextOrderNumber();
        Order order = Order.create(createOrderRequest.userId(), nextOrderNumber, orderItems);
        Order savedOrder = orderRepository.save(order);
        publishOrderCreatedEvent(savedOrder);
        log.info("Order created for user: {} with order id: {}", createOrderRequest.userId(), savedOrder.getId());
        return orderMapper.toCreateOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public CancelOrderResponse cancelOrder(CancelOrderRequest cancelOrderRequest) {
        var orderId = cancelOrderRequest.orderId();
        var userId = cancelOrderRequest.userId();
        var cancellationReason = cancelOrderRequest.reason();
        log.info("Cancelling order {} Reason: {}", orderId, cancellationReason);
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new OrderNotFoundException("No order exists with ID: " + orderId));
        if (!userId.equals(order.getUserId())) {
            throw new OrderNotFoundException("No order exists with ID: " + orderId + " for user with ID: " + userId);
        }
        order.cancel();
        Order savedOrder = orderRepository.save(order);
        publishOrderCancelledEvent(savedOrder, cancellationReason);
        log.info("Order {} cancelled successfully.", orderId);
        return orderMapper.toCancelOrderResponse(savedOrder, cancellationReason);
    }

    private Map<String, CatalogResponse.CatalogProductResponse> fetchCatalogData(CreateOrderRequest createOrderRequest) {
        CatalogRequest catalogRequest = new CatalogRequest(createOrderRequest.orderItems().stream()
                .map(orderItem -> new CatalogRequest.CatalogProductRequest(orderItem.productId(), orderItem.quantity()))
                .toList());
        return catalogClient.fetchProducts(catalogRequest).products().stream()
                .collect(Collectors.toMap(CatalogResponse.CatalogProductResponse::productId, Function.identity()));
    }

    private List<OrderItem> buildOrderItems(CreateOrderRequest createOrderRequest, Map<String, CatalogResponse.CatalogProductResponse> catalogProductDataMap) {
        return createOrderRequest.orderItems().stream()
                .map(orderItemRequest -> {
                    CatalogResponse.CatalogProductResponse product = catalogProductDataMap.get(orderItemRequest.productId());
                    validateProduct(orderItemRequest.productId(), product);
                    return orderMapper.toOrderItem(orderItemRequest, product);
                })
                .toList();
    }

    private void validateProduct(String productId,
                                 CatalogResponse.CatalogProductResponse productData) {
        // 1. Check Existence & Availability
        if (Boolean.FALSE.equals(productData.available())) {
            throw new ProductNotFoundException("Product not available: " + productId);
        }
        // 2. Product ID must not be blank
        if (productId.isBlank()) {
            throw new InvalidOrderStateException("Invalid id for product: " + productId);
        }
        // 3. Business Rule: Price must be positive
        if (productData.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOrderStateException("Invalid price for product: " + productId);
        }
    }

    private void publishOrderCreatedEvent(Order savedOrder) {
        OrderCreatedEvent event = orderMapper.toOrderCreatedEvent(savedOrder);
        orderEventPublisher.publishOrderCreated(event);
    }

    private void publishOrderCancelledEvent(Order savedOrder, String cancellationReason) {
        OrderCancelledEvent event = orderMapper.toOrderCancelledEvent(savedOrder, cancellationReason);
        orderEventPublisher.publishOrderCancelled(event);
    }

}
