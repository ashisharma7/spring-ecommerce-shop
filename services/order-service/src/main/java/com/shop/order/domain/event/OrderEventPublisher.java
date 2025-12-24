package com.shop.order.domain.event;

import com.shop.order.domain.exception.EventPublishingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final KafkaTemplate<@NonNull String, @NonNull Object> kafkaTemplate;
    private static final String TOPIC_ORDER_CREATED = "order-events";
    private static final String TOPIC_ORDER_CANCELLED = "order-cancelled-events";


    public void publishOrderCreated(OrderCreatedEvent event) {
        log.info("Publishing OrderCreatedEvent for Order: {}", event.orderId());
        try {
            kafkaTemplate.send(TOPIC_ORDER_CREATED, event.orderId(), event)
                    .get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            // In a real corporate app, we would use the Outbox Pattern here
            // to ensure we don't lose the event if Kafka is down.
            // For now, logging the error is the minimum requirement.
            log.error("Failed to publish OrderCreatedEvent for order: {}", event.orderId(), e);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new EventPublishingException("Failed to publish order event", e);
        }
    }

}