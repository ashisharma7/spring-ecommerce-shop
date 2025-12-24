package com.shop.order.web.controller;

import com.shop.order.service.OrderCommandService;
import com.shop.order.service.OrderQueryService;
import com.shop.order.web.dto.*;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    @GetMapping("/{orderId}")
    public ResponseEntity<@NonNull OrderResponse> getOrder(@PathVariable String orderId) {
        OrderResponse response = orderQueryService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<@NonNull CreateOrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest createOrderRequest
    ){
        CreateOrderResponse createOrderResponse = orderCommandService.createOrder(createOrderRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createOrderResponse);
    }

    @PostMapping("/cancel")
    public ResponseEntity<@NonNull CancelOrderResponse> cancelOrder(
            @Valid @RequestBody CancelOrderRequest cancelOrderRequest
    ){
        CancelOrderResponse cancelOrderResponse = orderCommandService.cancelOrder(cancelOrderRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cancelOrderResponse);
    }

}
