package com.shop.order.service;

import com.shop.order.domain.exception.OrderNotFoundException;
import com.shop.order.domain.model.Order;
import com.shop.order.repository.OrderRepository;
import com.shop.order.service.impl.OrderQueryServiceImpl;
import com.shop.order.testutil.TestData;
import com.shop.order.web.dto.OrderResponse;
import com.shop.order.web.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderQueryServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Spy
    private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @InjectMocks
    private OrderQueryServiceImpl orderQueryService;

    @Test
    void shouldReturnOrderResponse_WhenOrderExists() {
        Order order = TestData.createSavedOrderWithItems();
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        OrderResponse orderResponse = orderQueryService.getOrderById(UUID.randomUUID().toString());

        assertEquals(order.getOrderNumber(), orderResponse.orderNumber());
        assertEquals(order.getTotalAmount(), orderResponse.totalAmount());
        assertEquals(order.getStatus().toString(), orderResponse.status());
        assertEquals(order.getOrderItems().size(), orderResponse.items().size());
    }

    @Test
    void shouldThrowOrderNotFound_WhenIdDoesNotExist() {
        String id = UUID.randomUUID().toString();
        when(orderRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());

        OrderNotFoundException ex = assertThrows(OrderNotFoundException.class, () ->
                orderQueryService.getOrderById(id));

        assertThat(ex.getMessage())
                .contains(id);
    }
}