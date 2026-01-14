package com.shop.order.domain.model;

import com.shop.order.domain.exception.InvalidOrderStateException;
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
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    private BigDecimal totalAmount;

    @Embedded
    private DeliveryAddress deliveryAddress;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    public static Order create(@NotBlank String userId, @NotBlank Long orderNumber,
                               @NotNull DeliveryAddress deliveryAddress) {
        return Order.builder()
                .userId(userId)
                .orderNumber("ORD-"+orderNumber)
                .status(OrderStatus.CREATED)
                .totalAmount(BigDecimal.ZERO)
                .deliveryAddress(deliveryAddress)
                .orderItems(new ArrayList<>())
                .createdAt(Instant.now())
                .build();
    }

    public static Order create(@NotBlank String userId, @NotBlank Long orderNumber,
                               @NotNull DeliveryAddress deliveryAddress, @NotNull List<OrderItem> orderItems) {
        Order order = create(userId, orderNumber, deliveryAddress);
        orderItems.forEach(order::addItem);
        return order;
    }

    public void addItem(OrderItem orderItem) {
        orderItem.assignToOrder(this);
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
        if (OrderStatus.CANCELLED.equals(this.status)) {
            throw new InvalidOrderStateException("Cannot cancel an already cancelled Order.");
        }
        this.status = OrderStatus.CANCELLED;
    }

    @PrePersist
    @PreUpdate
    public void validateOrderState() {
        // 1. Must have at least one item
        if (this.orderItems == null || this.orderItems.isEmpty()) {
            throw new InvalidOrderStateException("Cannot save Order: Order must contain at least one item.");
        }

        // 2. Total amount must be positive
        if (this.totalAmount == null || this.totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOrderStateException("Cannot save Order: Total amount must be greater than zero.");
        }

        // 3. Delivery Address must be there
        if (this.deliveryAddress == null) {
            throw new InvalidOrderStateException("Cannot save Order: Delivery Address must not be null.");
        }

        // 4. Validating fields of delivery address
        this.deliveryAddress.validateAddress();
    }

}


