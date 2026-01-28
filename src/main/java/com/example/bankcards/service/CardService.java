package com.example.bankcards.service;

import com.example.bankcards.dto.card.CardCreateRequest;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.entity.*;
import com.example.bankcards.exception.ApiException;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.CardMasker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final CardBlockRequestRepository blockRequestRepository;
    private final UserService userService;

    public CardService(CardRepository cardRepository,
                       CardBlockRequestRepository blockRequestRepository,
                       UserService userService) {
        this.cardRepository = cardRepository;
        this.blockRequestRepository = blockRequestRepository;
        this.userService = userService;
    }

    @Transactional
    public CardResponse adminCreateCardByUserId(CardCreateRequest req) {
        User owner = userService.getByIdOrThrow(req.ownerUserId());
        String last4 = CardMasker.last4(req.cardNumber());
        if (last4 == null) throw new ApiException("INVALID_CARD_NUMBER", "Card number must contain at least 4 digits");
        Card c = new Card(owner, req.cardNumber().replaceAll("\\s+", ""), last4, req.expiry(), req.initialBalance());
        Card saved = cardRepository.save(c);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<CardResponse> userListMyCards(long ownerId, CardStatus status, String last4, Pageable pageable) {
        return cardRepository.findMyCards(ownerId, status, last4, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<CardResponse> adminListAll(Pageable pageable) {
        return cardRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public CardResponse userGetMyCard(long ownerId, long cardId) {
        Card c = cardRepository.findByIdAndOwner_Id(cardId, ownerId)
                .orElseThrow(() -> new ApiException("CARD_NOT_FOUND", "Card not found"));
        return toResponse(c);
    }

    @Transactional
    public void adminSetStatus(long cardId, CardStatus status) {
        Card c = cardRepository.findById(cardId)
                .orElseThrow(() -> new ApiException("CARD_NOT_FOUND", "Card not found"));
        c.setStatus(status);
    }

    @Transactional
    public void adminDelete(long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new ApiException("CARD_NOT_FOUND", "Card not found");
        }
        cardRepository.deleteById(cardId);
    }

    @Transactional
    public void userRequestBlock(long ownerId, long cardId) {
        Card c = cardRepository.findByIdAndOwner_Id(cardId, ownerId)
                .orElseThrow(() -> new ApiException("CARD_NOT_FOUND", "Card not found"));

        expireIfNeeded(c);

        if (c.getStatus() == CardStatus.BLOCKED) {
            throw new ApiException("CARD_ALREADY_BLOCKED", "Card already blocked");
        }
        if (c.getStatus() == CardStatus.EXPIRED) {
            throw new ApiException("CARD_EXPIRED", "Card expired");
        }
        blockRequestRepository.findByCard_IdAndStatus(cardId, BlockRequestStatus.PENDING)
                .ifPresent(x -> {
                    throw new ApiException("BLOCK_REQUEST_EXISTS", "Block request already exists");
                });

        User requester = userService.getByIdOrThrow(ownerId);
        blockRequestRepository.save(new CardBlockRequest(c, requester));
    }

    @Transactional
    public void adminApproveBlockRequest(long requestId, boolean approve) {
        CardBlockRequest req = blockRequestRepository.findById(requestId)
                .orElseThrow(() -> new ApiException("BLOCK_REQUEST_NOT_FOUND", "Block request not found"));

        if (req.getStatus() != BlockRequestStatus.PENDING) {
            throw new ApiException("BLOCK_REQUEST_ALREADY_PROCESSED", "Block request already processed");
        }

        if (approve) {
            req.setStatus(BlockRequestStatus.APPROVED);
            req.getCard().setStatus(CardStatus.BLOCKED);
        } else {
            req.setStatus(BlockRequestStatus.REJECTED);
        }
    }

    @Transactional
    public void expireIfNeeded(Card card) {
        LocalDate today = LocalDate.now();
        if (card.getExpiry().isBefore(today) && card.getStatus() != CardStatus.EXPIRED) {
            card.setStatus(CardStatus.EXPIRED);
        }
    }

    @Transactional(readOnly = true)
    public void validateTransferCardUsable(Card c) {
        if (c.getStatus() == CardStatus.BLOCKED) throw new ApiException("CARD_BLOCKED", "Card is blocked");
        if (c.getStatus() == CardStatus.EXPIRED) throw new ApiException("CARD_EXPIRED", "Card expired");
    }

    public CardResponse toResponse(Card c) {
        return new CardResponse(
                c.getId(),
                CardMasker.mask(c.getCardNumberPlain()),
                c.getOwner().getUsername(),
                c.getExpiry(),
                c.getStatus(),
                c.getBalance(),
                c.getCreatedAt()
        );
    }
}

