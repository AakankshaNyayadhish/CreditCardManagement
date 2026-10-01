package com.ofss.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ofss.entity.Transaction;

public interface TransactionRepository
        extends CrudRepository<Transaction, Long> {

    List<Transaction>
    findByCardNumberOrderByTransactionDateTimeDesc(
            String cardNumber
    );

    List<Transaction>
    findAllByOrderByTransactionDateTimeDesc();

    List<Transaction>
    findByTransactionDateTimeBetweenOrderByTransactionDateTimeDesc(
            LocalDateTime from,
            LocalDateTime to
    );

    List<Transaction>
    findByMerchantIdOrderByTransactionDateTimeDesc(
            Long merchantId
    );

    List<Transaction>
    findByAmountBetweenOrderByAmountDesc(
            Double minimum,
            Double maximum
    );
}