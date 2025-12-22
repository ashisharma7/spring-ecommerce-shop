package com.shop.order.repository;

import com.shop.order.domain.model.Order;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<@NonNull Order, @NonNull UUID> {
    Optional<Order> findByOrderNumber(String orderNumber);
}
