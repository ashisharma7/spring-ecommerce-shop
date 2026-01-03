package com.shop.order.web.exception;

import java.time.Instant;

public record ErrorResponse(
        ErrorCode error,
        String messages,
        Instant timestamp
) {
    public static ErrorResponse validationError(String messages) {
        return new ErrorResponse(
                ErrorCode.VALIDATION_ERROR,
                messages,
                Instant.now()
        );
    }

    public static ErrorResponse catalogUnavailableError(String messages) {
        return new ErrorResponse(
                ErrorCode.CATALOG_UNAVAILABLE,
                messages,
                Instant.now()
        );
    }

    public static ErrorResponse orderNotFoundError(String messages) {
        return new ErrorResponse(
                ErrorCode.ORDER_NOT_FOUND,
                messages,
                Instant.now()
        );
    }

    public static ErrorResponse productNotFoundError(String messages) {
        return new ErrorResponse(
                ErrorCode.PRODUCT_NOT_FOUND,
                messages,
                Instant.now()
        );
    }

    public static ErrorResponse invalidDomainStateError(String messages) {
        return new ErrorResponse(
                ErrorCode.DOMAIN_STATE_ERROR,
                messages,
                Instant.now()
        );
    }

    public static ErrorResponse internalServerError(String messages) {
        return new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR,
                messages,
                Instant.now()
        );
    }
}
