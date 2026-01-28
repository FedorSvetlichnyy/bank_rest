package com.example.bankcards.dto.card;

import com.example.bankcards.entity.CardStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record CardResponse(
        Long id,
        String maskedNumber,
        String ownerUsername,
        LocalDate expiry,
        CardStatus status,
        BigDecimal balance,
        Instant createdAt
) {
}

