package com.example.bankcards.controller;

import com.example.bankcards.dto.user.UserCreateRequest;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.entity.Role;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse create(@Valid @RequestBody UserCreateRequest req) {
        return userService.createUser(req);
    }

    @GetMapping
    public List<UserResponse> list() {
        return userService.listUsers();
    }

    @PatchMapping("/{id}/enabled")
    public void setEnabled(@PathVariable long id, @RequestParam boolean enabled) {
        userService.setEnabled(id, enabled);
    }

    @PatchMapping("/{id}/role")
    public void setRole(@PathVariable long id, @RequestParam Role role) {
        userService.setRole(id, role);
    }
}

