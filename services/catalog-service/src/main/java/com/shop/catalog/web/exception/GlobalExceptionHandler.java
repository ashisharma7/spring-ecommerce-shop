package com.shop.catalog.web.exception;

import com.shop.catalog.domain.exception.CategoryNotFoundException;
import com.shop.catalog.domain.exception.InvalidCategoryStateException;
import com.shop.catalog.domain.exception.InvalidProductStateException;
import com.shop.catalog.domain.exception.ProductNotFoundException;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        String errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(",\n"));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.validationError(errorMessages));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleCategoryNotFoundException(
            Exception ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.categoryNotFoundError(ex.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleProductNotFoundException(
            Exception ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.productNotFoundError(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCategoryStateException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleInvalidCategoryStateException(
            Exception ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.invalidDomainStateError(ex.getMessage()));
    }

    @ExceptionHandler(InvalidProductStateException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleInvalidProductStateException(
            Exception ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.invalidDomainStateError(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ErrorResponse> handleGenericException(
            Exception ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.internalServerError(ex.getMessage()));
    }

}
