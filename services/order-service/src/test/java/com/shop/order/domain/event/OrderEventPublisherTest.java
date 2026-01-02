package com.shop.order.domain.event;

import com.shop.order.domain.exception.EventPublishingException;
import com.shop.order.testutil.TestData;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderEventPublisherTest {

    @Mock
    private KafkaTemplate<@NonNull String, @NonNull Object> kafkaTemplate;

    @InjectMocks
    private OrderEventPublisher orderEventPublisher;

    @Test
    void shouldPublishOrderCreatedEvent_Successfully() {
        OrderCreatedEvent orderCreatedEvent = TestData.getOrderCreatedEvent();

        when(kafkaTemplate.send("order-events", orderCreatedEvent.orderId(), orderCreatedEvent))
                .thenReturn(CompletableFuture.completedFuture(null));

        orderEventPublisher.publishOrderCreated(orderCreatedEvent);

        verify(kafkaTemplate, times(1))
                .send("order-events", orderCreatedEvent.orderId(), orderCreatedEvent);
    }

    @Test
    void shouldPublishOrderCancelledEvent_Successfully() {
        OrderCancelledEvent orderCancelledEvent = TestData.getOrderCancelledEvent();

        when(kafkaTemplate.send("order-cancelled-events", orderCancelledEvent.orderId(), orderCancelledEvent))
                .thenReturn(CompletableFuture.completedFuture(null));

        orderEventPublisher.publishOrderCancelled(orderCancelledEvent);

        verify(kafkaTemplate, times(1))
                .send("order-cancelled-events", orderCancelledEvent.orderId(), orderCancelledEvent);
    }

    @Test
    void shouldPublishOrderCreatedEventThrowEventPublishingException_WhenKafkaFails() {
        OrderCreatedEvent orderCreatedEvent = TestData.getOrderCreatedEvent();

        when(kafkaTemplate.send("order-events", orderCreatedEvent.orderId(), orderCreatedEvent))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        EventPublishingException eventPublishingException = assertThrows(EventPublishingException.class, () ->
                orderEventPublisher.publishOrderCreated(orderCreatedEvent));

        verify(kafkaTemplate, times(1))
                .send("order-events", orderCreatedEvent.orderId(), orderCreatedEvent);

        assertThat(eventPublishingException.getMessage())
                .contains("Failed to publish order event");
    }

    @Test
    void shouldPublishOrderCreatedEventHandleInterruptedException_AndResetThreadFlag() throws Exception{
        OrderCreatedEvent orderCreatedEvent = TestData.getOrderCreatedEvent();
        CompletableFuture<SendResult<@NonNull String, @NonNull Object>> interruptedFuture =
                mock(String.valueOf(CompletableFuture.class));

        when(interruptedFuture.get(anyLong(), any(TimeUnit.class)))
                .thenThrow(new InterruptedException("Simulated interruption"));
        when(kafkaTemplate.send(any(), any(), any()))
                .thenReturn(interruptedFuture);

        EventPublishingException eventPublishingException = assertThrows(EventPublishingException.class, () ->
                orderEventPublisher.publishOrderCreated(orderCreatedEvent));

        assertThat(Thread.currentThread().isInterrupted()).isTrue(); //checking for interrupt
        assertTrue(Thread.interrupted()); // clearing interrupt
        assertThat(eventPublishingException.getMessage())
                .contains("Failed to publish order event");

        verify(kafkaTemplate, times(1))
                .send("order-events", orderCreatedEvent.orderId(), orderCreatedEvent);
    }

    @Test
    void shouldPublishOrderCancelledEventThrowEventPublishingException_WhenKafkaFails() {
        OrderCancelledEvent orderCancelledEvent = TestData.getOrderCancelledEvent();

        when(kafkaTemplate.send("order-cancelled-events", orderCancelledEvent.orderId(), orderCancelledEvent))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        EventPublishingException eventPublishingException = assertThrows(EventPublishingException.class, () ->
                orderEventPublisher.publishOrderCancelled(orderCancelledEvent));

        verify(kafkaTemplate, times(1))
                .send("order-cancelled-events", orderCancelledEvent.orderId(), orderCancelledEvent);

        assertThat(eventPublishingException.getMessage())
                .contains("Failed to publish order cancelled event");
    }

    @Test
    void shouldPublishOrderCancelledEventHandleInterruptedException_AndResetThreadFlag() throws Exception{
        OrderCancelledEvent orderCancelledEvent = TestData.getOrderCancelledEvent();
        CompletableFuture<SendResult<@NonNull String, @NonNull Object>> interruptedFuture =
                mock(String.valueOf(CompletableFuture.class));

        when(interruptedFuture.get(anyLong(), any(TimeUnit.class)))
                .thenThrow(new InterruptedException("Simulated interruption"));
        when(kafkaTemplate.send("order-cancelled-events", orderCancelledEvent.orderId(), orderCancelledEvent))
                .thenReturn(interruptedFuture);

        EventPublishingException eventPublishingException = assertThrows(EventPublishingException.class, () ->
                orderEventPublisher.publishOrderCancelled(orderCancelledEvent));

        assertThat(Thread.currentThread().isInterrupted()).isTrue(); //checking for interrupt
        assertTrue(Thread.interrupted()); // clearing interrupt
        assertThat(eventPublishingException.getMessage())
                .contains("Failed to publish order cancelled event");

        verify(kafkaTemplate, times(1))
                .send("order-cancelled-events", orderCancelledEvent.orderId(), orderCancelledEvent);
    }
}