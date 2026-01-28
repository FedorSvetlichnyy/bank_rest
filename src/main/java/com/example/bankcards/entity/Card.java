package com.example.bankcards.entity;

import com.example.bankcards.util.CardNumberCryptoConverter;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "cards",
        indexes = {
                @Index(name = "idx_cards_owner_id", columnList = "owner_id"),
                @Index(name = "idx_cards_last4", columnList = "last4"),
                @Index(name = "idx_cards_status", columnList = "status")
        })
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Convert(converter = CardNumberCryptoConverter.class)
    @Column(name = "card_number_enc", nullable = false, length = 512)
    private String cardNumberEncrypted;

    @Column(name = "last4", nullable = false, length = 4)
    private String last4;

    @Column(name = "expiry", nullable = false)
    private LocalDate expiry;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private CardStatus status = CardStatus.ACTIVE;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    protected Card() {
    }

    public Card(User owner, String plainCardNumber, String last4, LocalDate expiry, BigDecimal balance) {
        this.owner = owner;
        this.cardNumberEncrypted = plainCardNumber; // encrypted by converter
        this.last4 = last4;
        this.expiry = expiry;
        this.status = CardStatus.ACTIVE;
        this.balance = balance == null ? BigDecimal.ZERO : balance;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String getCardNumberPlain() {
        return cardNumberEncrypted; // decrypted by converter
    }

    public String getLast4() {
        return last4;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public CardStatus getStatus() {
        return status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}

