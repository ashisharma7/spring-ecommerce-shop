package com.shop.order.catalog.exception;

public class CatalogUnavailableException extends RuntimeException {
    public CatalogUnavailableException() {
        super("Catalog service is unavailable");
    }
    public CatalogUnavailableException(Exception exception) {
        super("Catalog service is unavailable", exception);
    }
}