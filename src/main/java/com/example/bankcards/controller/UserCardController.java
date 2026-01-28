package com.example.bankcards.controller;

import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.security.CurrentUser;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class UserCardController {
    private final UserService userService;
    private final CardService cardService;

    public UserCardController(UserService userService, CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }

    @GetMapping
    public Page<CardResponse> myCards(
            @RequestParam(required = false) CardStatus status,
            @RequestParam(required = false) String last4,
            Pageable pageable
    ) {
        User me = userService.getByUsernameOrThrow(CurrentUser.username());
        return cardService.userListMyCards(me.getId(), status, last4, pageable);
    }

    @GetMapping("/{id}")
    public CardResponse myCard(@PathVariable long id) {
        User me = userService.getByUsernameOrThrow(CurrentUser.username());
        return cardService.userGetMyCard(me.getId(), id);
    }

    @GetMapping("/{id}/balance")
    public String balance(@PathVariable long id) {
        User me = userService.getByUsernameOrThrow(CurrentUser.username());
        return cardService.userGetMyCard(me.getId(), id).balance().toPlainString();
    }

    @PostMapping("/{id}/block-request")
    public void requestBlock(@PathVariable long id) {
        User me = userService.getByUsernameOrThrow(CurrentUser.username());
        cardService.userRequestBlock(me.getId(), id);
    }
}

