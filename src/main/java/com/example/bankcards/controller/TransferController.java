package com.example.bankcards.controller;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.dto.transfer.TransferResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.security.CurrentUser;
import com.example.bankcards.service.TransferService;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class TransferController {
    private final UserService userService;
    private final TransferService transferService;

    public TransferController(UserService userService, TransferService transferService) {
        this.userService = userService;
        this.transferService = transferService;
    }

    @PostMapping
    public TransferResponse transfer(@Valid @RequestBody TransferRequest req) {
        User me = userService.getByUsernameOrThrow(CurrentUser.username());
        return transferService.transferBetweenMyCards(me.getId(), req);
    }
}

