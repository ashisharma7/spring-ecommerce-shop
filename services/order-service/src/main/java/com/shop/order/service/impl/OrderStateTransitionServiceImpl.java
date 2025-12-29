package com.shop.order.service.impl;

import com.shop.order.domain.event.OrderCancelledEvent;
import com.shop.order.domain.event.OrderEventPublisher;
import com.shop.order.domain.model.Order;
import com.shop.order.repository.OrderRepository;
import com.shop.order.service.OrderStateTransitionService;
import com.shop.order.web.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStateTransitionServiceImpl implements OrderStateTransitionService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public Order markPendingPayment(Order order) {
        log.info("Transitioning Order {} to PENDING_PAYMENT", order.getId());
        order.markPendingPayment();
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order confirmOrder(Order order) {
        log.info("Transitioning Order {} to CONFIRMED", order.getId());
        order.confirm();
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order cancelOrder(Order order, String reason) {
        log.info("Transitioning Order {} to CANCELLED", order.getId());
        order.cancel();
        Order savedOrder = orderRepository.save(order);
        publishOrderCancelledEvent(savedOrder, reason);
        return savedOrder;
    }

    private void publishOrderCancelledEvent(Order order, String reason){
        var orderCancelledEvent = orderMapper.toOrderCancelledEvent(order, reason);
        orderEventPublisher.publishOrderCancelled(orderCancelledEvent);
    }

}
