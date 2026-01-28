package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByIdAndOwner_Id(Long id, Long ownerId);

    @Query("""
            select c from Card c
            where c.owner.id = :ownerId
              and (:status is null or c.status = :status)
              and (:last4 is null or c.last4 = :last4)
            """)
    @EntityGraph(attributePaths = {"owner"})
    Page<Card> findMyCards(@Param("ownerId") Long ownerId,
                           @Param("status") CardStatus status,
                           @Param("last4") String last4,
                           Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"owner"})
    Page<Card> findAll(Pageable pageable);
}

