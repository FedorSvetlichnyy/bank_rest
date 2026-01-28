package com.example.bankcards.dto.block;

import com.example.bankcards.entity.BlockRequestStatus;

import java.time.Instant;

public record BlockRequestResponse(
        Long id,
        Long cardId,
        String requestedByUsername,
        BlockRequestStatus status,
        Instant createdAt
) {
}

