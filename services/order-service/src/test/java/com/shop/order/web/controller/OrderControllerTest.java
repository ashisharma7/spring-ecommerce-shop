package com.shop.order.web.controller;

import com.shop.order.service.OrderCommandService;
import com.shop.order.service.OrderQueryService;
import com.shop.order.testutil.TestData;
import com.shop.order.web.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderCommandService orderCommandService;

    @MockitoBean
    private OrderQueryService orderQueryService;

    @Test
    void shouldCreateOrder_AndReturn201_WhenRequestIsValid() throws Exception {
        CreateOrderRequest createOrderRequest = TestData.createValidCreateOrderRequest();
        CreateOrderResponse createOrderResponse = TestData.createValidCreateOrderResponse();

        when(orderCommandService.createOrder(any()))
                .thenReturn(createOrderResponse);

        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNumber").value(createOrderResponse.orderNumber()))
                .andExpect(jsonPath("$.totalAmount").value(createOrderResponse.totalAmount()));
    }

    @Test
    void shouldGetOrder_AndReturn200_WhenOrderExists() throws Exception {
        OrderResponse orderResponse = TestData.createValidOrderResponse();

        when(orderQueryService.getOrderById(any()))
                .thenReturn(orderResponse);

        mockMvc.perform(get("/api/orders/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderResponse.orderId()))
                .andExpect(jsonPath("$.orderNumber").value(orderResponse.orderNumber()))
                .andExpect(jsonPath("$.totalAmount").value(orderResponse.totalAmount()))
                .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    void shouldCancelOrder_AndReturn200_WhenRequestIsValid() throws Exception {
        // Covers: POST /api/orders/{id}/cancel
        CancelOrderRequest cancelOrderRequest = TestData.createValidCancelOrderRequest();
        CancelOrderResponse cancelOrderResponse = TestData.createValidCancelOrderResponse();

        when(orderCommandService.cancelOrder(cancelOrderRequest))
                .thenReturn(cancelOrderResponse);

        mockMvc.perform(post("/api/orders/cancel")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelOrderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(cancelOrderResponse.orderId()))
                .andExpect(jsonPath("$.status").value(cancelOrderResponse.status()))
                .andExpect(jsonPath("$.reason").value(cancelOrderResponse.reason()));
    }

    @Test
    void shouldFailWhenItemsAreEmpty() throws Exception {
        String payload = """
                {
                  "userId": "u1",
                  "items": []
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void validationErrorReturnsStructuredError() throws Exception {
        String payload = "{}";

        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.messages").isString());
    }

}
