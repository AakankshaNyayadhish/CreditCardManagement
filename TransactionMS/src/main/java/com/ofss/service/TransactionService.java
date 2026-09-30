package com.ofss.service;

import com.ofss.entity.Transaction;
import com.ofss.enums.TransactionStatus;
import com.ofss.enums.TransactionType;
import com.ofss.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final RestClient restClient;

	private final String cardServiceUrl;
	private final String merchantServiceUrl;

	public TransactionService(TransactionRepository transactionRepository, RestClient.Builder restClientBuilder,
			@Value("${services.card.url}") String cardServiceUrl,
			@Value("${services.merchant.url}") String merchantServiceUrl) {

		this.transactionRepository = transactionRepository;
		this.cardServiceUrl = cardServiceUrl;
		this.merchantServiceUrl = merchantServiceUrl;
		this.restClient = restClientBuilder.build();
	}

	@Transactional
	public Transaction processPurchase(Transaction transaction) {

		transaction.setTransactionType(TransactionType.PURCHASE);
		transaction.setTransactionDateTime(LocalDateTime.now());

		try {
			if (!checkMerchantExists(transaction.getMerchantId())) {
				transaction.setStatus(TransactionStatus.FAILED);
				return transactionRepository.save(transaction);
			}
			Boolean success = processCardPurchase(transaction.getCardNumber(), transaction.getAmount());

			if (Boolean.TRUE.equals(success)) {
				transaction.setStatus(TransactionStatus.SUCCESS);
			} else {
				transaction.setStatus(TransactionStatus.FAILED);
			}

		} catch (Exception ex) {
			transaction.setStatus(TransactionStatus.FAILED);
		}

		return transactionRepository.save(transaction);
	}

	@Transactional
	public Transaction processPayment(Transaction transaction) {

		transaction.setTransactionType(TransactionType.PAYMENT);
		transaction.setMerchantId(null);
		transaction.setTransactionDateTime(LocalDateTime.now());

		try {
			Boolean success = processCardPayment(transaction.getCardNumber(), transaction.getAmount());

			if (Boolean.TRUE.equals(success)) {
				transaction.setStatus(TransactionStatus.SUCCESS);
			} else {
				transaction.setStatus(TransactionStatus.FAILED);
			}

		} catch (Exception ex) {

			transaction.setStatus(TransactionStatus.FAILED);
		}

		return transactionRepository.save(transaction);
	}

	public List<Transaction> getAllTransactions() {

		return transactionRepository.findAllByOrderByTransactionDateTimeDesc();
	}

	public Transaction getTransactionById(Long transactionId) {

		return transactionRepository.findById(transactionId).orElse(null);
	}

	public List<Transaction> getTransactionsByCard(String cardNumber) {

		return transactionRepository.findByCardNumberOrderByTransactionDateTimeDesc(cardNumber);
	}

	private boolean checkMerchantExists(Long merchantId) {

		try {

			restClient.get().uri(merchantServiceUrl + "/merchants/{merchantId}", merchantId).retrieve()
					.toBodilessEntity();

			return true;

		} catch (Exception ex) {

			return false;
		}
	}

	private Boolean processCardPurchase(String cardNumber, Double amount) {

		return restClient.post()
				.uri(cardServiceUrl + "/internal/cards/{cardNumber}/purchase?amount={amount}", cardNumber, amount)
				.retrieve().body(Boolean.class);
	}

	private Boolean processCardPayment(String cardNumber, Double amount) {

		return restClient.post()
				.uri(cardServiceUrl + "/internal/cards/{cardNumber}/payment?amount={amount}", cardNumber, amount)
				.retrieve().body(Boolean.class);
	}
}