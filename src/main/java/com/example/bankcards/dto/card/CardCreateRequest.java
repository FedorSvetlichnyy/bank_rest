package com.example.bankcards.dto.card;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardCreateRequest(
        @NotNull Long ownerUserId,
        @NotBlank
        @Pattern(regexp = "^[0-9 ]{12,25}$", message = "Card number must contain digits/spaces only")
        String cardNumber,
        @NotNull LocalDate expiry,
        @NotNull @DecimalMin(value = "0.00") BigDecimal initialBalance
) {
}

