package com.ofss.controller;

import com.ofss.entity.Transaction;
import com.ofss.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

	@GetMapping("/reports/purchases/today")
	public ResponseEntity<Double> todayPurchaseTotal() {
		return ResponseEntity.ok(transactionService.getTodayPurchaseTotal());
	}

	@GetMapping("/reports/payments/today")
	public ResponseEntity<Double> todayPaymentTotal() {
		return ResponseEntity.ok(transactionService.getTodayPaymentTotal());
	}

	@GetMapping("/reports/merchants/highest-sales")
	public ResponseEntity<Map<String, Object>> merchantWithHighestSales() {
		return ResponseEntity.ok(transactionService.getMerchantWithHighestSales());
	}

	@GetMapping("/reports/merchants/most-transactions")
	public ResponseEntity<Map<String, Object>> merchantWithMostTransactions() {
		return ResponseEntity.ok(transactionService.getMerchantWithMostTransactions());
	}

	@GetMapping("/reports/cards/most-used")
	public ResponseEntity<Map<String, Object>> mostUsedCard() {
		return ResponseEntity.ok(transactionService.getMostUsedCard());
	}

	@GetMapping("/reports/cards/least-used")
	public ResponseEntity<Map<String, Object>> leastUsedCard() {
		return ResponseEntity.ok(transactionService.getLeastUsedCard());
	}

	@GetMapping("/reports/customers/highest-spending")
	public ResponseEntity<Map<String, Object>> highestSpendingCustomer() {
		return ResponseEntity.ok(transactionService.getCustomerWithHighestSpending());
	}

	@GetMapping("/reports/customers/highest-payment")
	public ResponseEntity<Map<String, Object>> highestPaymentCustomer() {
		return ResponseEntity.ok(transactionService.getCustomerWithHighestPayment());
	}

	@GetMapping("/reports/purchases/average")
	public ResponseEntity<Double> averagePurchaseAmount() {
		return ResponseEntity.ok(transactionService.getAveragePurchaseAmount());
	}

	@GetMapping("/reports/purchases/largest")
	public ResponseEntity<Transaction> largestPurchase() {
		return ResponseEntity.ok(transactionService.getLargestPurchase());
	}

	@GetMapping("/reports/customers/monthly-spending")
	public ResponseEntity<List<Map<String, Object>>> monthlyCustomerSpending() {
		return ResponseEntity.ok(transactionService.getMonthlyCustomerSpending());
	}
}
