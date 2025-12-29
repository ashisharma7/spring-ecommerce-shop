package com.shop.order.service;

import com.shop.order.domain.model.Order;

public interface OrderStateTransitionService {
    Order markPendingPayment(Order order);
    Order confirmOrder(Order order);
    Order cancelOrder(Order order, String reason);
}
