package com.example.bankcards.controller;

import com.example.bankcards.dto.card.CardCreateRequest;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/cards")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCardController {
    private final CardService cardService;

    public AdminCardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public CardResponse create(@Valid @RequestBody CardCreateRequest req) {
        return cardService.adminCreateCardByUserId(req);
    }

    @GetMapping
    public Page<CardResponse> listAll(Pageable pageable) {
        return cardService.adminListAll(pageable);
    }

    @PatchMapping("/{id}/status")
    public void setStatus(@PathVariable long id, @RequestParam CardStatus status) {
        cardService.adminSetStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        cardService.adminDelete(id);
    }
}

