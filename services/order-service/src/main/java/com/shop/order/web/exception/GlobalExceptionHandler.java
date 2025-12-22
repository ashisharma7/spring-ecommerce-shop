package com.shop.order.web.exception;

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
                .body(new ErrorResponse(
                        "VALIDATION_ERROR",
                        errors,
                        Instant.now()
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "VALIDATION_ERROR",
                        List.of(ex.getMessage()),
                        Instant.now()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ErrorResponse> handleGenericException(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "INTERNAL_SERVER_ERROR",
                        List.of("Something went wrong"),
                        Instant.now()
                ));
    }
}
