package com.ofss.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ofss.entity.Transaction;
import com.ofss.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class TransactionServiceSearchTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldSearchTransactionsByCard() {

        Transaction transaction = new Transaction();
        transaction.setCardNumber("4222222222222222");

        when(transactionRepository
                .findByCardNumberOrderByTransactionDateTimeDesc(
                        "4222222222222222"))
                .thenReturn(List.of(transaction));

        List<Transaction> result =
                transactionService.getTransactionsByCard(
                        "4222222222222222");

        assertEquals(1, result.size());
        assertEquals(
                "4222222222222222",
                result.get(0).getCardNumber()
        );
    }

    @Test
    void shouldSearchTransactionsByDateRange() {

        LocalDateTime from =
                LocalDateTime.of(2026, 9, 1, 0, 0);

        LocalDateTime to =
                LocalDateTime.of(2026, 9, 30, 23, 59);

        when(transactionRepository
                .findByTransactionDateTimeBetweenOrderByTransactionDateTimeDesc(
                        from, to))
                .thenReturn(List.of(new Transaction()));

        List<Transaction> result =
        		transactionService.searchByDateRange(
                        from, to);

        assertEquals(1, result.size());
    }
}