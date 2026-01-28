package com.example.bankcards.service;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.dto.transfer.TransferResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.exception.ApiException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {
    private final CardRepository cardRepository;
    private final TransferRepository transferRepository;
    private final CardService cardService;

    public TransferService(CardRepository cardRepository, TransferRepository transferRepository, CardService cardService) {
        this.cardRepository = cardRepository;
        this.transferRepository = transferRepository;
        this.cardService = cardService;
    }

    @Transactional
    public TransferResponse transferBetweenMyCards(long ownerId, TransferRequest req) {
        if (req.fromCardId().equals(req.toCardId())) {
            throw new ApiException("SAME_CARD", "fromCardId and toCardId must be different");
        }

        Card from = cardRepository.findByIdAndOwner_Id(req.fromCardId(), ownerId)
                .orElseThrow(() -> new ApiException("CARD_NOT_FOUND", "From-card not found"));
        Card to = cardRepository.findByIdAndOwner_Id(req.toCardId(), ownerId)
                .orElseThrow(() -> new ApiException("CARD_NOT_FOUND", "To-card not found"));

        cardService.expireIfNeeded(from);
        cardService.expireIfNeeded(to);
        cardService.validateTransferCardUsable(from);
        cardService.validateTransferCardUsable(to);

        BigDecimal amount = req.amount();
        if (from.getBalance().compareTo(amount) < 0) {
            throw new ApiException("INSUFFICIENT_FUNDS", "Insufficient funds");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        Transfer t = transferRepository.save(new Transfer(from, to, amount));
        return new TransferResponse(t.getId(), from.getId(), to.getId(), t.getAmount(), t.getCreatedAt());
    }
}

