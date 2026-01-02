package com.shop.order.catalog;

import com.shop.order.catalog.dto.CatalogProductRequest;
import com.shop.order.catalog.dto.CatalogProductResponse;
import com.shop.order.catalog.exception.CatalogUnavailableException;
import com.shop.order.catalog.exception.ProductNotFoundException;
import com.shop.order.catalog.impl.HttpCatalogClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest(HttpCatalogClient.class)
@TestPropertySource(properties = "catalog.client.enforce-timeouts=false")
class CatalogClientTest {

    @Autowired
    private HttpCatalogClient catalogClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    private final String catalogUri = "http://localhost:8082/internal/catalog/products/info";

    @Test
    void shouldReturnProducts_WhenApiCallIsSuccessful() {
        var request = List.of(new CatalogProductRequest("prod-1", 2));
        var catalogProductResponseJson = """
            {
                "products": [
                    { "productId": "prod-1", "name": "Phone", "price": 500.00, "available": true }
                ]
            }
            """;

        mockServer.expect(requestTo(catalogUri))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(catalogProductResponseJson, MediaType.APPLICATION_JSON));

        List<CatalogProductResponse> result = catalogClient.fetchProducts(request);

        // 3. Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().productId()).isEqualTo("prod-1");
        assertThat(result.getFirst().price()).isEqualByComparingTo("500.00");
    }

    @Test
    void shouldThrowProductNotFound_WhenServerReturns4xx() {
        var request = List.of(new CatalogProductRequest("invalid", 1));

        mockServer.expect(requestTo(catalogUri))
                .andRespond(withBadRequest());

        assertThatThrownBy(() -> catalogClient.fetchProducts(request))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void shouldThrowCatalogUnavailable_WhenServerReturns5xx() {
        var request = List.of(new CatalogProductRequest("invalid", 1));

        mockServer.expect(requestTo(catalogUri))
                .andRespond(withServerError());

        assertThatThrownBy(() -> catalogClient.fetchProducts(request))
                .isInstanceOf(CatalogUnavailableException.class);
    }

    @Test
    void shouldThrowCatalogUnavailable_WhenResourceAccessExceptionOccurs() {
        var request = List.of(new CatalogProductRequest("invalid", 1));

        mockServer.expect(requestTo(catalogUri))
                .andRespond(withException(new IOException("Simulated IOException")));

        assertThatThrownBy(() -> catalogClient.fetchProducts(request))
                .isInstanceOf(CatalogUnavailableException.class);
    }

    @Test
    void shouldWrapGenericException_WhenUnexpectedErrorOccurs() {
        var request = List.of(new CatalogProductRequest("invalid", 1));

        mockServer.expect(requestTo(catalogUri))
                .andRespond(req -> {
                    throw new RuntimeException("Simulated IOException");
                });

        assertThatThrownBy(() -> catalogClient.fetchProducts(request))
                .isInstanceOf(RuntimeException.class);
    }

}