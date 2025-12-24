package com.shop.order.service;

import com.shop.order.web.dto.OrderResponse;

public interface OrderQueryService {
    OrderResponse getOrderById(String orderId);
}