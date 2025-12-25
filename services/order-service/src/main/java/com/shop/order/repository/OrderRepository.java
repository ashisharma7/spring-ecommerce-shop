package com.shop.order.repository;

import com.shop.order.domain.model.Order;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<@NonNull Order, @NonNull UUID> {
    @Query(value = "SELECT nextval('order_number_seq')", nativeQuery = true)
    Long getNextOrderNumber();

    Optional<Order> findByOrderNumber(String orderNumber);
}
