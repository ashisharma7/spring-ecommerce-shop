package com.shop.order.web.exception;

import com.shop.order.catalog.exception.CatalogUnavailableException;
import com.shop.order.catalog.exception.ProductNotFoundException;
import com.shop.order.domain.exception.EventPublishingException;
import com.shop.order.domain.exception.OrderNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.validationError(errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.validationError(List.of(ex.getMessage())));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.productNotFoundError(List.of(ex.getMessage())));
    }

    @ExceptionHandler(CatalogUnavailableException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleCatalogUnavailable(CatalogUnavailableException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ErrorResponse.catalogUnavailableError(List.of(ex.getMessage())));
    }

    @ExceptionHandler(EventPublishingException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleEventPublishingException(EventPublishingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.internalServerError(List.of(ex.getMessage())));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.orderNotFoundError(List.of(ex.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.internalServerError(List.of("Something went wrong", ex.getMessage())));
    }
}
