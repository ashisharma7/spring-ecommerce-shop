package com.shop.order.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    //Domain helper methods
    public static Order create(@NotBlank String userId) {
        return Order.builder()
                .userId(userId)
                .orderNumber(UUID.randomUUID().toString())
                .status(OrderStatus.CREATED)
                .build();
    }

    public static Order create(@NotBlank String userId, @NotNull List<OrderItem> orderItems) {
        Order order = create(userId);
        orderItems.forEach(order::addItem);
        return order;
    }

    public void addItem(OrderItem orderItem) {
        orderItem.setOrder(this);
        this.orderItems.add(orderItem);
        BigDecimal itemTotal = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
        this.totalAmount = this.totalAmount.add(itemTotal);
    }

    public void markPendingPayment() {
        this.status = OrderStatus.PENDING_PAYMENT;
    }

    public void confirm() {
        this.status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
    }
}


