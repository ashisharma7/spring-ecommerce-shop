package com.shop.order.web.exception;

import com.shop.order.catalog.exception.CatalogUnavailableException;
import com.shop.order.catalog.exception.ProductNotFoundException;
import com.shop.order.domain.exception.EventPublishingException;
import com.shop.order.domain.exception.InvalidOrderStateException;
import com.shop.order.domain.exception.OrderNotFoundException;
import com.shop.order.service.OrderCommandService;
import com.shop.order.service.OrderQueryService;
import com.shop.order.testutil.TestData;
import com.shop.order.web.controller.OrderController;
import com.shop.order.web.dto.CreateOrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderCommandService orderCommandService;

    @MockitoBean
    private OrderQueryService orderQueryService;

    @Test
    void shouldReturn400_WhenValidationFails() throws Exception {
        CreateOrderRequest createOrderRequest = TestData.createInvalidCreateOrderRequest_BlankUserID();

        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldReturn400_WhenProductNotFound_DuringCreateOrder() throws Exception {
        CreateOrderRequest createOrderRequest = TestData.createValidCreateOrderRequest();

        when(orderCommandService.createOrder(createOrderRequest))
                .thenThrow(new ProductNotFoundException("Simulated product not found"));

        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("PRODUCT_NOT_FOUND"))
                .andExpect(jsonPath("$.messages").value("Simulated product not found"));
    }

    @Test
    void shouldReturn503_WhenCatalogUnavailable_DuringCreateOrder() throws Exception {
        CreateOrderRequest createOrderRequest = TestData.createValidCreateOrderRequest();

        when(orderCommandService.createOrder(createOrderRequest))
                .thenThrow(new CatalogUnavailableException());

        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("CATALOG_UNAVAILABLE"))
                .andExpect(jsonPath("$.messages").value("Catalog service is unavailable"));
    }

    @Test
    void shouldReturn400_WhenOrderNotFound_DuringGetOrder() throws Exception {
        when(orderQueryService.getOrderById(any()))
                .thenThrow(new OrderNotFoundException("Simulated order not found"));

        mockMvc.perform(get("/api/orders/123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ORDER_NOT_FOUND"))
                .andExpect(jsonPath("$.messages").value("Simulated order not found"));
    }

    @Test
    void shouldReturn500_WhenEventPublishingFails() throws Exception {
        CreateOrderRequest createOrderRequest = TestData.createValidCreateOrderRequest();

        when(orderCommandService.createOrder(createOrderRequest))
                .thenThrow(new EventPublishingException("Simulated event not published", null));

        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.messages").value("Simulated event not published"));
    }

    @Test
    void shouldReturn409_WhenInvalidOrderState() throws Exception {
        CreateOrderRequest createOrderRequest = TestData.createValidCreateOrderRequest();

        when(orderCommandService.createOrder(createOrderRequest))
                .thenThrow(new InvalidOrderStateException("Simulated invalid order state"));

        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("DOMAIN_STATE_ERROR"))
                .andExpect(jsonPath("$.messages").value("Simulated invalid order state"));
    }

    @Test
    void shouldReturn500_WhenUnexpectedErrorOccurs() throws Exception {
        when(orderQueryService.getOrderById(any()))
                .thenThrow(new RuntimeException("Simulated generic exception"));

        mockMvc.perform(get("/api/orders/123"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.messages").value("Simulated generic exception"));
    }
}