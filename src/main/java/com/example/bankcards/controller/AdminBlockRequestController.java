package com.example.bankcards.controller;

import com.example.bankcards.dto.block.BlockRequestResponse;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.service.CardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/block-requests")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBlockRequestController {
    private final CardBlockRequestRepository repo;
    private final CardService cardService;

    public AdminBlockRequestController(CardBlockRequestRepository repo, CardService cardService) {
        this.repo = repo;
        this.cardService = cardService;
    }

    @GetMapping
    public List<BlockRequestResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @PostMapping("/{id}/approve")
    public void approve(@PathVariable long id) {
        cardService.adminApproveBlockRequest(id, true);
    }

    @PostMapping("/{id}/reject")
    public void reject(@PathVariable long id) {
        cardService.adminApproveBlockRequest(id, false);
    }

    private BlockRequestResponse toResponse(CardBlockRequest r) {
        return new BlockRequestResponse(
                r.getId(),
                r.getCard().getId(),
                r.getRequestedBy().getUsername(),
                r.getStatus(),
                r.getCreatedAt()
        );
    }
}

