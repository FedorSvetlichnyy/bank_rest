package com.example.bankcards.dto.auth;

public record LoginResponse(
        String tokenType,
        String accessToken
) {
}

