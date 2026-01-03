package com.shop.order.service;

import com.shop.order.catalog.CatalogClient;
import com.shop.order.catalog.dto.CatalogResponse;
import com.shop.order.catalog.exception.ProductNotFoundException;
import com.shop.order.domain.event.OrderCancelledEvent;
import com.shop.order.domain.event.OrderCreatedEvent;
import com.shop.order.domain.event.OrderEventPublisher;
import com.shop.order.domain.exception.InvalidOrderStateException;
import com.shop.order.domain.exception.OrderNotFoundException;
import com.shop.order.domain.model.Order;
import com.shop.order.repository.OrderRepository;
import com.shop.order.service.impl.OrderCommandServiceImpl;
import com.shop.order.testutil.TestData;
import com.shop.order.web.dto.CancelOrderRequest;
import com.shop.order.web.dto.CancelOrderResponse;
import com.shop.order.web.dto.CreateOrderRequest;
import com.shop.order.web.dto.CreateOrderResponse;
import com.shop.order.web.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderCommandServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventPublisher eventPublisher;

    @Mock
    private CatalogClient catalogClient;

    @Spy
    private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @InjectMocks
    private OrderCommandServiceImpl orderCommandService;

    @Test
    void shouldCreateOrderSuccessfullyAndPublishEvent_WhenCatalogIsAvailable() {
        CreateOrderRequest request = TestData.createValidCreateOrderRequest();
        CatalogResponse catalogProducts = new CatalogResponse(List.of(TestData.createValidCatalogProduct()));
        Long orderNumber = TestData.getRandomNumber();
        Order savedOrder = TestData.createSavedOrderWithItems();

        when(catalogClient.fetchProducts(any()))
                .thenReturn(catalogProducts);
        when(orderRepository.getNextOrderNumber())
                .thenReturn(orderNumber);
        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        CreateOrderResponse response = orderCommandService.createOrder(request);

        assertThat(response.orderNumber())
                .isEqualTo(savedOrder.getOrderNumber());
        verify(orderRepository)
                .getNextOrderNumber();
        verify(orderRepository)
                .save(any(Order.class));
        verify(eventPublisher)
                .publishOrderCreated(any(OrderCreatedEvent.class));
    }

    @Test
    void shouldThrowProductNotFound_WhenCatalogReturnsNullOrEmpty() {
        CreateOrderRequest request = TestData.createValidCreateOrderRequest();
        when(catalogClient.fetchProducts(any()))
                .thenThrow(new ProductNotFoundException("Product not found in catalog"));

        ProductNotFoundException productNotFoundException = assertThrows(ProductNotFoundException.class, () ->
                orderCommandService.createOrder(request));
        assertThat(productNotFoundException.getMessage())
                .contains("not found");
    }

    @Test
    void shouldThrowProductNotFound_WhenProductIsNotAvailableInCatalog() {
        CreateOrderRequest request = TestData.createValidCreateOrderRequest();
        CatalogResponse catalogProducts = new CatalogResponse(
                List.of(TestData.createInvalidCatalogProduct_NotAvailable())
        );
        when(catalogClient.fetchProducts(any()))
                .thenReturn(catalogProducts);

        ProductNotFoundException productNotFoundException = assertThrows(ProductNotFoundException.class, () ->
                orderCommandService.createOrder(request));
        assertThat(productNotFoundException.getMessage())
                .contains("not available");
    }

    @Test
    void shouldThrowInvalidOrderState_WhenProductPriceIsZeroOrNegative() {
        CreateOrderRequest request = TestData.createValidCreateOrderRequest();
        CatalogResponse catalogProducts = new CatalogResponse(
                List.of(TestData.createInvalidCatalogProduct_NegativePrice())
        );
        when(catalogClient.fetchProducts(any()))
                .thenReturn(catalogProducts);

        InvalidOrderStateException invalidOrderStateException = assertThrows(InvalidOrderStateException.class, () ->
                orderCommandService.createOrder(request));
        assertThat(invalidOrderStateException.getMessage())
                .contains("Invalid price for product");
    }

    @Test
    void shouldThrowInvalidOrderState_WhenProductIdIsBlank() {
        CreateOrderRequest request = TestData.createInvalidCreateOrderRequest_BlankProductID();
        CatalogResponse catalogProducts = new CatalogResponse(
                List.of(TestData.createInvalidCatalogProduct_BlankID())
        );
        when(catalogClient.fetchProducts(any()))
                .thenReturn(catalogProducts);

        InvalidOrderStateException invalidOrderStateException = assertThrows(InvalidOrderStateException.class, () ->
                orderCommandService.createOrder(request));
        assertThat(invalidOrderStateException.getMessage())
                .contains("Invalid id for product");
    }

    // --- Cancel Order Tests ---

    @Test
    void shouldCancelOrderSuccessfullyAndPublishEvent_WhenUserOwnsOrder() {
        long orderNumber = TestData.getRandomNumber();
        CancelOrderRequest cancelOrderRequest = TestData.createValidCancelOrderRequest();
        Order savedOrder = TestData.createSavedOrderWithItems(orderNumber);
        Order cancelledOrder = TestData.createSavedOrderWithItems(orderNumber);

        when(orderRepository.findById(any()))
                .thenReturn(Optional.of(savedOrder));
        when(orderRepository.save(any()))
                .thenReturn(cancelledOrder);

        CancelOrderResponse cancelOrderResponse = orderCommandService.cancelOrder(cancelOrderRequest);

        assertEquals(cancelledOrder.getId().toString(), cancelOrderResponse.orderId());
        assertEquals("Cancel my order", cancelOrderResponse.reason());

        verify(orderRepository)
                .save(any(Order.class));
        verify(eventPublisher)
                .publishOrderCancelled(any(OrderCancelledEvent.class));
    }

    @Test
    void shouldThrowOrderNotFound_WhenOrderDoesNotExist() {
        CancelOrderRequest cancelOrderRequest = TestData.createValidCancelOrderRequest();

        when(orderRepository.findById(any()))
                .thenReturn(Optional.empty());

        OrderNotFoundException orderNotFoundException = assertThrows(OrderNotFoundException.class, () ->
                orderCommandService.cancelOrder(cancelOrderRequest));

        assertThat(orderNotFoundException.getMessage())
                .contains("No order exists with ID");
    }

    @Test
    void shouldThrowInvalidOrderState_WhenUserDoesNotOwnOrder() {
        // Covers: cancelOrder() -> !order.getUserId().equals(request.userId())
        long orderNumber = TestData.getRandomNumber();
        CancelOrderRequest cancelOrderRequest = TestData.createInvalidCancelOrderRequest_WrongUserID();
        Order savedOrder = TestData.createSavedOrderWithItems(orderNumber);

        when(orderRepository.findById(any()))
                .thenReturn(Optional.of(savedOrder));

        OrderNotFoundException orderNotFoundException = assertThrows(OrderNotFoundException.class, () ->
                orderCommandService.cancelOrder(cancelOrderRequest));

        assertThat(orderNotFoundException.getMessage())
                .contains("No order exists with ID")
                .contains("for user with ID");
    }
}
