package com.ofss.repository;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ofss.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long>{
	List<Transaction> findByCardNumberOrderByTransactionDateTimeDesc(String cardNumber);

	List<Transaction> findAllByOrderByTransactionDateTimeDesc();

	ArrayList<Transaction> findByCardNumber(String cardNumber);

}
