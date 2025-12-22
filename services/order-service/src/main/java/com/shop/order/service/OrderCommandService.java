package com.shop.order.service;

import com.shop.order.web.dto.CreateOrderRequest;
import com.shop.order.web.dto.CreateOrderResponse;

public interface OrderCommandService {
    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest);
}
