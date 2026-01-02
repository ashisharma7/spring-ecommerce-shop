package com.shop.order.domain.model;

import com.shop.order.domain.exception.InvalidOrderStateException;
import com.shop.order.testutil.TestData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDomainTest {

    @Test
    void shouldCreateOrder_WithInitialStateCreated_AndZeroTotal() {
        long orderNumber = TestData.getRandomNumber();
        Order order = TestData.createNotSavedOrderWithoutItems(orderNumber);

        assertNull(order.getId());
        assertEquals("user-1", order.getUserId());
        assertEquals("ORD-" + orderNumber, order.getOrderNumber());
        assertEquals(BigDecimal.ZERO, order.getTotalAmount());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertTrue(order.getOrderItems().isEmpty());
        assertNotNull(order.getCreatedAt());
    }

    @Test
    void shouldCreateOrder_WithItems_AndCalculateTotalCorrectly() {
        long orderNumber = TestData.getRandomNumber();
        Order order = TestData.createNotSavedOrderWithItems(orderNumber);
        BigDecimal totalAmountShouldBe = BigDecimal.valueOf(1500);

        assertNull(order.getId());
        assertEquals("user-1", order.getUserId());
        assertEquals("ORD-" + orderNumber, order.getOrderNumber());
        assertEquals(totalAmountShouldBe, order.getTotalAmount());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertFalse(order.getOrderItems().isEmpty());
        assertNotNull(order.getCreatedAt());
    }

    @Test
    void shouldAddItem_AndRecalculateTotalAmount() {
        long orderNumber = TestData.getRandomNumber();
        Order order = TestData.createNotSavedOrderWithItems(orderNumber);
        BigDecimal totalAmountShouldBe = BigDecimal.valueOf(1500);

        assertNull(order.getId());
        assertEquals("user-1", order.getUserId());
        assertEquals("ORD-" + orderNumber, order.getOrderNumber());
        assertEquals(totalAmountShouldBe, order.getTotalAmount());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertFalse(order.getOrderItems().isEmpty());
        assertEquals(5, order.getOrderItems().size());
        assertNotNull(order.getCreatedAt());

        order.addItem(TestData.createOrderItem());
        BigDecimal newTotalAmountShouldBe = BigDecimal.valueOf(2500);

        assertEquals(newTotalAmountShouldBe, order.getTotalAmount());
        assertEquals(6, order.getOrderItems().size());
    }

    @Test
    void shouldMarkPendingPayment() {
        long orderNumber = TestData.getRandomNumber();
        Order order = TestData.createNotSavedOrderWithItems(orderNumber);
        BigDecimal totalAmountShouldBe = BigDecimal.valueOf(1500);

        assertNull(order.getId());
        assertEquals("user-1", order.getUserId());
        assertEquals("ORD-" + orderNumber, order.getOrderNumber());
        assertEquals(totalAmountShouldBe, order.getTotalAmount());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertFalse(order.getOrderItems().isEmpty());
        assertEquals(5, order.getOrderItems().size());
        assertNotNull(order.getCreatedAt());

        order.markPendingPayment();
        assertEquals(OrderStatus.PENDING_PAYMENT, order.getStatus());
    }

    @Test
    void shouldConfirmOrder() {
        long orderNumber = TestData.getRandomNumber();
        Order order = TestData.createNotSavedOrderWithItems(orderNumber);
        BigDecimal totalAmountShouldBe = BigDecimal.valueOf(1500);

        assertNull(order.getId());
        assertEquals("user-1", order.getUserId());
        assertEquals("ORD-" + orderNumber, order.getOrderNumber());
        assertEquals(totalAmountShouldBe, order.getTotalAmount());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertFalse(order.getOrderItems().isEmpty());
        assertEquals(5, order.getOrderItems().size());
        assertNotNull(order.getCreatedAt());

        order.confirm();
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    void shouldCancelOrder() {
        long orderNumber = TestData.getRandomNumber();
        Order order = TestData.createNotSavedOrderWithItems(orderNumber);
        BigDecimal totalAmountShouldBe = BigDecimal.valueOf(1500);

        assertNull(order.getId());
        assertEquals("user-1", order.getUserId());
        assertEquals("ORD-" + orderNumber, order.getOrderNumber());
        assertEquals(totalAmountShouldBe, order.getTotalAmount());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertFalse(order.getOrderItems().isEmpty());
        assertEquals(5, order.getOrderItems().size());
        assertNotNull(order.getCreatedAt());

        order.cancel();
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void shouldNotThrowException_WhenSavingOrder() {
        Order zeroAmountOrder = Order.create("user-1", 100L);
        OrderItem orderItem = OrderItem.builder()
                .price(BigDecimal.ONE)
                .quantity(1)
                .build();
        zeroAmountOrder.addItem(orderItem);

        assertDoesNotThrow(zeroAmountOrder::validateOrderState);
    }

    @Test
    void shouldThrowException_WhenSavingOrderWithNullItems() {
        Order nullItemsOrder = Order.builder()
                .orderItems(null)
                .build();

        assertThrows(InvalidOrderStateException.class, nullItemsOrder::validateOrderState);
    }

    @Test
    void shouldThrowException_WhenSavingOrderWithNoItems() {
        Order emptyOrder = Order.create("user-1", 100L);

        assertThrows(InvalidOrderStateException.class, emptyOrder::validateOrderState);
    }

    @Test
    void shouldThrowException_WhenSavingOrderWithNullTotalAmount() {
        Order nullAmountOrder = Order.builder()
                .orderItems(List.of(OrderItem.builder().build()))
                .totalAmount(null)
                .build();

        assertThrows(InvalidOrderStateException.class, nullAmountOrder::validateOrderState);
    }

    @Test
    void shouldThrowException_WhenSavingOrderWithZeroTotalAmount() {
        Order zeroAmountOrder = Order.create("user-1", 100L);
        OrderItem orderItem = OrderItem.builder()
                .price(BigDecimal.ZERO)
                .quantity(1)
                .build();
        zeroAmountOrder.addItem(orderItem);

        assertThrows(InvalidOrderStateException.class, zeroAmountOrder::validateOrderState);
    }

}