package com.ofss.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ofss.entity.Transaction;
import com.ofss.enums.TransactionStatus;
import com.ofss.enums.TransactionType;
import com.ofss.repository.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository tr;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${card.service.url}")
	private String cardServiceUrl;

	@Value("${merchant.service.url}")
	private String merchantServiceUrl;


	public Transaction processPurchase(Transaction entry) {

		entry.setTransactionType(TransactionType.PURCHASE);
		entry.setTransactionDateTime(LocalDateTime.now());

	
		  String merchantUrl = merchantServiceUrl + "/merchants/" +
		  entry.getMerchantId();
		 
		  restTemplate.getForEntity( merchantUrl, Object.class );
		 
		  String cardUrl = cardServiceUrl + "/internal/cards/" + entry.getCardNumber()
		  + "/purchase?amount=" + entry.getAmount();
		 
		  Boolean success = restTemplate.postForObject( cardUrl, null, Boolean.class );
		 
		  if (Boolean.TRUE.equals(success)) {
		  entry.setStatus(TransactionStatus.SUCCESS); } else {
		  entry.setStatus(TransactionStatus.FAILED); }
		  
//		entry.setStatus(TransactionStatus.SUCCESS);

		return tr.save(entry);
	}

	public Transaction processPayment(Transaction entry) {

		entry.setTransactionType(TransactionType.PAYMENT);

		entry.setMerchantId(null);

		entry.setTransactionDateTime(LocalDateTime.now());

		String cardUrl = cardServiceUrl + "/internal/cards/" + entry.getCardNumber() + "/payment?amount="
				+ entry.getAmount();

		Boolean success = restTemplate.postForObject(cardUrl, null, Boolean.class);

		if (Boolean.TRUE.equals(success)) {
			entry.setStatus(TransactionStatus.SUCCESS);
		} else {
			entry.setStatus(TransactionStatus.FAILED);
		}

//		entry.setStatus(TransactionStatus.SUCCESS);

		return tr.save(entry);
	}

	public List<Transaction> getAllTransactions() {

		return tr.findAllByOrderByTransactionDateTimeDesc();
	}

	public List<Transaction> getTransactionsByCard(String cardNumber) {

		return tr.findByCardNumberOrderByTransactionDateTimeDesc(cardNumber);
	}

	public Transaction getTransactionById(Long transactionId) {

		Optional<Transaction> transaction = tr.findById(transactionId);

		return transaction.orElse(null);
	}
}