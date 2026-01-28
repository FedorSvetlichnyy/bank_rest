package com.example.bankcards.exception;

import java.time.Instant;
import java.util.Map;

public record ApiErrorResponse(
        String code,
        String message,
        Instant timestamp,
        Map<String, String> details
) {
}

