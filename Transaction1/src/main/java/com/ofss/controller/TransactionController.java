package com.ofss.controller;

import com.ofss.entity.Transaction;
import com.ofss.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping("/purchases")
	public ResponseEntity<Transaction> purchase(@RequestBody Transaction transaction) {

		Transaction savedTransaction = transactionService.processPurchase(transaction);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
	}

	@PostMapping("/payments")
	public ResponseEntity<Transaction> payment(@RequestBody Transaction transaction) {

		Transaction savedTransaction = transactionService.processPayment(transaction);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
	}

	@GetMapping
	public ResponseEntity<List<Transaction>> getAllTransactions() {

		return ResponseEntity.ok(transactionService.getAllTransactions());
	}

	@GetMapping("/{transactionId}")
	public ResponseEntity<Transaction> getTransactionById(@PathVariable Long transactionId) {

		Transaction transaction = transactionService.getTransactionById(transactionId);

		if (transaction != null) {
			return ResponseEntity.ok(transaction);
		}

		return ResponseEntity.notFound().build();
	}

	@GetMapping("/card/{cardNumber}")
	public ResponseEntity<List<Transaction>> getTransactionsByCard(@PathVariable String cardNumber) {

		List<Transaction> transactions = transactionService.getTransactionsByCard(cardNumber);

		if (transactions != null && !transactions.isEmpty()) {
			return ResponseEntity.ok(transactions);
		}

		return ResponseEntity.notFound().build();
	}
}