package com.shop.order.service.impl;

import com.shop.order.catalog.CatalogClient;
import com.shop.order.catalog.dto.CatalogProductRequest;
import com.shop.order.catalog.dto.CatalogProductResponse;
import com.shop.order.catalog.exception.ProductNotFoundException;
import com.shop.order.domain.exception.InvalidOrderStateException;
import com.shop.order.domain.model.Order;
import com.shop.order.domain.model.OrderItem;
import com.shop.order.repository.OrderRepository;
import com.shop.order.service.OrderCommandService;
import com.shop.order.web.dto.CreateOrderItemRequest;
import com.shop.order.web.dto.CreateOrderRequest;
import com.shop.order.web.dto.CreateOrderResponse;
import com.shop.order.web.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements OrderCommandService {
    private final OrderRepository orderRepository;
    private final CatalogClient catalogClient;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) {
        log.info("Processing create order request for user: {}", createOrderRequest.userId());
        Map<String, CatalogProductResponse> catalogProductDataMap = fetchCatalogData(createOrderRequest);
        List<OrderItem> orderItems = buildOrderItems(createOrderRequest, catalogProductDataMap);
        Order order = Order.create(createOrderRequest.userId(), orderItems);
        Order savedOrder = orderRepository.save(order);
        log.info("Order created for user: {} with order id: {}", createOrderRequest.userId(), savedOrder.getId());
        return orderMapper.toCreateOrderResponse(savedOrder);
    }

    private Map<String, CatalogProductResponse> fetchCatalogData(CreateOrderRequest createOrderRequest) {
        List<CatalogProductRequest> catalogProductRequestList = createOrderRequest.orderItems().stream()
                .map(orderItem -> new CatalogProductRequest(orderItem.productId(), orderItem.quantity()))
                .toList();
        return catalogClient.fetchProducts(catalogProductRequestList).stream()
                .collect(Collectors.toMap(CatalogProductResponse::productId, Function.identity()));
    }

    private List<OrderItem> buildOrderItems(CreateOrderRequest createOrderRequest, Map<String, CatalogProductResponse> catalogProductDataMap) {
        return createOrderRequest.orderItems().stream()
                .map(orderItemRequest -> {
                    CatalogProductResponse product = catalogProductDataMap.get(orderItemRequest.productId());
                    validateProduct(orderItemRequest.productId(), product);
                    return orderMapper.toOrderItem(orderItemRequest, product);
                })
                .toList();
    }

    private void validateProduct(String productId,
                                                   CatalogProductResponse productData) {
        // 1. Check Existence & Availability
        if (null == productData || !Boolean.TRUE.equals(productData.available())) {
            throw new ProductNotFoundException("Product not available: " + productId);
        }
        // 2. Product ID must not be blank
        if(productId.isBlank()){
            throw new InvalidOrderStateException("Invalid id for product: " + productId);
        }
        // 3. Business Rule: Price must be positive
        if (productData.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOrderStateException("Invalid price for product: " + productId);
        }
    }

}
