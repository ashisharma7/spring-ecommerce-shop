package com.shop.order.service;

import com.shop.order.domain.event.OrderEventPublisher;
import com.shop.order.domain.exception.InvalidOrderStateException;
import com.shop.order.domain.model.Order;
import com.shop.order.domain.model.OrderStatus;
import com.shop.order.repository.OrderRepository;
import com.shop.order.service.impl.OrderStateTransitionServiceImpl;
import com.shop.order.testutil.TestData;
import com.shop.order.web.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderStateTransitionServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventPublisher orderEventPublisher;

    @Spy
    private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @InjectMocks
    private OrderStateTransitionServiceImpl orderStateTransitionService;

    @Test
    void shouldTransitionToPendingPayment_WhenStatusIsCreated() {
        Order order = TestData.createSavedOrderWithItems();

        when(orderRepository.save(any()))
                .thenReturn(order);

        Order savedOrder = orderStateTransitionService.markPendingPayment(order);

        assertEquals(OrderStatus.PENDING_PAYMENT, savedOrder.getStatus());
    }

    @Test
    void shouldTransitionToConfirmed_WhenStatusIsPendingPayment() {
        Order order = TestData.createSavedOrderWithItems();

        when(orderRepository.save(any()))
                .thenReturn(order);

        Order savedOrder = orderStateTransitionService.confirmOrder(order);

        assertEquals(OrderStatus.CONFIRMED, savedOrder.getStatus());
    }

    @Test
    void shouldCancelOrder_AndPublishEvent_WhenStateIsValid() {
        Order order = TestData.createSavedOrderWithItems();

        when(orderRepository.save(any()))
                .thenReturn(order);

        Order savedOrder = orderStateTransitionService.cancelOrder(order, "Cancel my order");

        assertEquals(OrderStatus.CANCELLED, savedOrder.getStatus());
        verify(orderEventPublisher, times(1))
                .publishOrderCancelled(any());
    }

    @Test
    void shouldThrowException_WhenCancelling_IfAlreadyCancelled() {
        Order order = TestData.createSavedOrderWithItems();
        order.cancel();

         assertThatThrownBy(() -> orderStateTransitionService.cancelOrder(order, "Cancel my order"))
                 .isInstanceOf(InvalidOrderStateException.class)
                 .hasMessageContaining("already cancelled");
    }

}
