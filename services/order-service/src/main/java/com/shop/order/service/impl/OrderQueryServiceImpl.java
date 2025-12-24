package com.shop.order.service.impl;

import com.shop.order.domain.exception.OrderNotFoundException;
import com.shop.order.domain.model.Order;
import com.shop.order.repository.OrderRepository;
import com.shop.order.service.OrderQueryService;
import com.shop.order.web.dto.OrderResponse;
import com.shop.order.web.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true) // Optimization for DB reads
@RequiredArgsConstructor
public class OrderQueryServiceImpl implements OrderQueryService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return orderMapper.toOrderResponse(order);
    }
}
