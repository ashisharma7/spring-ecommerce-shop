package com.shop.order.web.exception;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        String error,
        List<String> messages,
        Instant timestamp
) {}
