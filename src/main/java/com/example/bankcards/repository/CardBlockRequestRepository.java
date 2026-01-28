package com.example.bankcards.repository;

import com.example.bankcards.entity.BlockRequestStatus;
import com.example.bankcards.entity.CardBlockRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardBlockRequestRepository extends JpaRepository<CardBlockRequest, Long> {
    Optional<CardBlockRequest> findByCard_IdAndStatus(Long cardId, BlockRequestStatus status);
}

