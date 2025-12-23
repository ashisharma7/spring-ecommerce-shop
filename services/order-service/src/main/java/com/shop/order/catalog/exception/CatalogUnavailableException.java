package com.shop.order.catalog.exception;

public class CatalogUnavailableException extends RuntimeException {
    public CatalogUnavailableException() {
        super("Catalog service is unavailable");
    }
}