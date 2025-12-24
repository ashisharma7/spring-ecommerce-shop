package com.shop.order.web.controller;

import com.shop.order.service.OrderCommandService;
import com.shop.order.web.dto.CancelOrderRequest;
import com.shop.order.web.dto.CancelOrderResponse;
import com.shop.order.web.dto.CreateOrderRequest;
import com.shop.order.web.dto.CreateOrderResponse;
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
