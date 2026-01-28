package com.example.bankcards.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "card_block_requests")
public class CardBlockRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requested_by_user_id", nullable = false)
    private User requestedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private BlockRequestStatus status = BlockRequestStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    protected CardBlockRequest() {
    }

    public CardBlockRequest(Card card, User requestedBy) {
        this.card = card;
        this.requestedBy = requestedBy;
        this.status = BlockRequestStatus.PENDING;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Card getCard() {
        return card;
    }

    public User getRequestedBy() {
        return requestedBy;
    }

    public BlockRequestStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setStatus(BlockRequestStatus status) {
        this.status = status;
    }
}

