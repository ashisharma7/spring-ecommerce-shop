package com.shop.order.catalog.impl;

import com.shop.order.catalog.CatalogClient;
import com.shop.order.catalog.dto.CatalogProductRequest;
import com.shop.order.catalog.dto.CatalogProductResponse;
import com.shop.order.catalog.exception.CatalogUnavailableException;
import com.shop.order.catalog.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;

@Component
public class HttpCatalogClient implements CatalogClient {
    private static final String CATALOG_PRODUCT_CHECK_ENDPOINT = "/internal/catalog/products/price-check";
    private final RestClient restClient;

    public HttpCatalogClient(RestClient.Builder builder,
                             @Value("${catalog.base-url}") String baseUrl,
                             @Value("${catalog.client.connect-timeout-ms}") long connectTimeout,
                             @Value("${catalog.client.read-timeout-ms}") long readTimeout) {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) connectTimeout);
        requestFactory.setReadTimeout((int) readTimeout);
        restClient = builder.baseUrl(baseUrl).requestFactory(requestFactory).build();
    }

    @Override
    public List<CatalogProductResponse> fetchProducts(List<CatalogProductRequest> products) {
        try {
            return Objects.requireNonNull(restClient.post()
                    .uri(CATALOG_PRODUCT_CHECK_ENDPOINT)
                    .body(new CatalogRequest(products))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        throw new ProductNotFoundException("Product not found in catalog");
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new CatalogUnavailableException();
                    })
                    .body(CatalogResponse.class))
                    .products;
        } catch (ProductNotFoundException | CatalogUnavailableException catalogException) {
            throw catalogException;
        } catch (ResourceAccessException resourceAccessException){
            throw new CatalogUnavailableException();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private record CatalogRequest(List<CatalogProductRequest> items) {
    }

    private record CatalogResponse(List<CatalogProductResponse> products) {
    }

}
