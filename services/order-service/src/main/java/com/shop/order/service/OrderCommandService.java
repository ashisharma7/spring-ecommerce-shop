package com.shop.order.service;

import com.shop.order.web.dto.CreateOrderRequest;
import com.shop.order.web.dto.CreateOrderResponse;
import org.springframework.stereotype.Service;

public interface OrderCommandService {
    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest);
}
