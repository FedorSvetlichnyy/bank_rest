package com.example.bankcards.service;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TransferServiceTest {

    @Autowired
    TransferService transferService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransferRepository transferRepository;

    @Test
    void transferBetweenMyCards_movesMoneyAndCreatesTransfer() {
        User u = userRepository.save(new User("u1", "{noop}x", Role.USER));
        Card c1 = cardRepository.save(new Card(u, "4111111111111111", "1111", LocalDate.now().plusYears(2), new BigDecimal("100.00")));
        Card c2 = cardRepository.save(new Card(u, "5555555555554444", "4444", LocalDate.now().plusYears(2), new BigDecimal("10.00")));

        var resp = transferService.transferBetweenMyCards(u.getId(), new TransferRequest(c1.getId(), c2.getId(), new BigDecimal("25.50")));

        assertThat(resp.id()).isNotNull();
        assertThat(resp.amount()).isEqualByComparingTo("25.50");
        assertThat(transferRepository.count()).isEqualTo(1);

        Card after1 = cardRepository.findById(c1.getId()).orElseThrow();
        Card after2 = cardRepository.findById(c2.getId()).orElseThrow();
        assertThat(after1.getBalance()).isEqualByComparingTo("74.50");
        assertThat(after2.getBalance()).isEqualByComparingTo("35.50");
    }

    @Test
    void transferBetweenMyCards_rejectsInsufficientFunds() {
        User u = userRepository.save(new User("u2", "{noop}x", Role.USER));
        Card c1 = cardRepository.save(new Card(u, "4111111111111111", "1111", LocalDate.now().plusYears(2), new BigDecimal("1.00")));
        Card c2 = cardRepository.save(new Card(u, "5555555555554444", "4444", LocalDate.now().plusYears(2), new BigDecimal("10.00")));

        assertThatThrownBy(() -> transferService.transferBetweenMyCards(u.getId(), new TransferRequest(c1.getId(), c2.getId(), new BigDecimal("2.00"))))
                .hasMessageContaining("Insufficient");
    }
}

