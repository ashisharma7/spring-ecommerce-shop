package com.shop.catalog.domain.exception;

public class InvalidCategoryStateException extends RuntimeException {
    public InvalidCategoryStateException(String message) {
        super(message);
    }
}
