package com.shop.order.service;

import com.shop.order.web.dto.CancelOrderRequest;
import com.shop.order.web.dto.CancelOrderResponse;
import com.shop.order.web.dto.CreateOrderRequest;
import com.shop.order.web.dto.CreateOrderResponse;

public interface OrderCommandService {
    CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest);
    CancelOrderResponse cancelOrder(CancelOrderRequest request);
}
