package com.example.bankcards.dto.user;

import com.example.bankcards.entity.Role;

import java.time.Instant;

public record UserResponse(
        Long id,
        String username,
        Role role,
        boolean enabled,
        Instant createdAt
) {
}

