package com.ofss.repository;

import com.ofss.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findByCardNumberOrderByTransactionDateTimeDesc(String cardNumber);

	List<Transaction> findAllByOrderByTransactionDateTimeDesc();
}