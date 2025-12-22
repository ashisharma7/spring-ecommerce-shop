package com.shop.order.web.controller;

import com.shop.order.service.OrderCommandService;
import com.shop.order.web.dto.CreateOrderRequest;
import com.shop.order.web.dto.CreateOrderResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
