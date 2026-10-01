package com.ofss.controller;

import com.ofss.entity.Transaction;
import com.ofss.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
	
	@GetMapping("/search/date-range")
	public ResponseEntity<List<Transaction>> searchByDateRange(
	        @RequestParam LocalDateTime from,
	        @RequestParam LocalDateTime to) {

	    return ResponseEntity.ok(
	            transactionService.searchByDateRange(from, to)
	    );
	}

	@GetMapping("/search/merchant/{merchantId}")
	public ResponseEntity<List<Transaction>> searchByMerchant(
	        @PathVariable Long merchantId) {

	    return ResponseEntity.ok(
	            transactionService.searchByMerchant(merchantId)
	    );
	}

	@GetMapping("/search/amount")
	public ResponseEntity<List<Transaction>> searchByAmount(
	        @RequestParam Double minimum,
	        @RequestParam Double maximum) {

	    return ResponseEntity.ok(
	            transactionService.searchByAmount(minimum, maximum)
	    );
	}
	
	@GetMapping("/rewards/card/{cardNumber}")
	public ResponseEntity<Long> getRewardPointsByCard(
	        @PathVariable String cardNumber) {

	    Long totalPoints =
	            transactionService.getRewardPointsByCard(cardNumber);

	    return ResponseEntity.ok(totalPoints);
	}
	
	@GetMapping("/cashback/card/{cardNumber}")
	public ResponseEntity<Double> getCashbackByCard(
	        @PathVariable String cardNumber) {

	    Double cashback =
	            transactionService.getCashbackByCard(cardNumber);

	    return ResponseEntity.ok(cashback);
	}
	
	@GetMapping("/late-payment-penalty")
	public ResponseEntity<Double> calculateLatePaymentPenalty(
	        @RequestParam("cardNumber") String cardNumber,
	        @RequestParam("dueDate") String dueDate,
	        @RequestParam("paymentDate") String paymentDate) {

	    LocalDate due = LocalDate.parse(dueDate);
	    LocalDate payment = LocalDate.parse(paymentDate);

	    Double penalty = transactionService.calculateLatePaymentPenalty(
	            cardNumber,
	            due,
	            payment
	    );

	    return ResponseEntity.ok(penalty);
	}
}