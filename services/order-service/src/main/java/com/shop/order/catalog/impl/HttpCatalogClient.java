package com.shop.order.catalog.impl;

import com.shop.order.catalog.CatalogClient;
import com.shop.order.catalog.dto.CatalogRequest;
import com.shop.order.catalog.dto.CatalogResponse;
import com.shop.order.catalog.exception.CatalogUnavailableException;
import com.shop.order.catalog.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Component
public class HttpCatalogClient implements CatalogClient {
    private static final String CATALOG_PRODUCT_CHECK_ENDPOINT = "/internal/catalog/products/info";
    private final RestClient restClient;

    public HttpCatalogClient(RestClient.Builder builder,
                             @Value("${catalog.base-url}") String baseUrl,
                             @Value("${catalog.client.connect-timeout-ms}") long connectTimeout,
                             @Value("${catalog.client.read-timeout-ms}") long readTimeout,
                             @Value("${catalog.client.enforce-timeouts:true}") boolean enforceTimeouts) {

        builder.baseUrl(baseUrl);
        if (enforceTimeouts){
            var requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout((int) connectTimeout);
            requestFactory.setReadTimeout((int) readTimeout);
            builder.requestFactory(requestFactory);
        }
        this.restClient = builder.build();
    }

    @Override
    public CatalogResponse fetchProducts(CatalogRequest catalogRequest) {
        try {
            return Objects.requireNonNull(restClient.post()
                    .uri(CATALOG_PRODUCT_CHECK_ENDPOINT)
                    .body(catalogRequest)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        throw new ProductNotFoundException("Product not found in catalog");
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new CatalogUnavailableException();
                    })
                    .body(CatalogResponse.class));
        } catch (ProductNotFoundException | CatalogUnavailableException catalogException) {
            throw catalogException;
        } catch (ResourceAccessException resourceAccessException){
            throw new CatalogUnavailableException();
        } catch (Exception exception) {
            throw new CatalogUnavailableException(exception);
        }
    }

}
