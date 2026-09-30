package com.ofss.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

	@Value("${customer.service.url}")
	private String customerServiceUrl;


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

	public Map<String, Object> getMerchantWithHighestSales() {
		return merchantReport(successfulPurchases(), "totalSales", true);
	}

	public Map<String, Object> getMerchantWithMostTransactions() {
		return merchantReport(successfulPurchases(), "transactionCount", false);
	}

	public Map<String, Object> getMostUsedCard() {
		return cardUsageReport(true);
	}

	public Map<String, Object> getLeastUsedCard() {
		return cardUsageReport(false);
	}

	public double getTodayPurchaseTotal() {
		return successfulPurchases().stream()
				.filter(transaction -> transaction.getTransactionDateTime().toLocalDate().equals(LocalDate.now()))
				.mapToDouble(Transaction::getAmount).sum();
	}

	public double getTodayPaymentTotal() {
		return successfulTransactions(TransactionType.PAYMENT).stream()
				.filter(transaction -> transaction.getTransactionDateTime().toLocalDate().equals(LocalDate.now()))
				.mapToDouble(Transaction::getAmount).sum();
	}

	public Map<String, Object> getCustomerWithHighestSpending() {
		return customerAmountReport(TransactionType.PURCHASE, "totalSpending");
	}

	public Map<String, Object> getCustomerWithHighestPayment() {
		return customerAmountReport(TransactionType.PAYMENT, "totalPayment");
	}

	public double getAveragePurchaseAmount() {
		return successfulPurchases().stream().mapToDouble(Transaction::getAmount).average().orElse(0.0);
	}

	public Transaction getLargestPurchase() {
		return successfulPurchases().stream()
				.max(Comparator.comparing(Transaction::getAmount))
				.orElseThrow(() -> new IllegalStateException("No successful purchases are available."));
	}

	public List<Map<String, Object>> getMonthlyCustomerSpending() {
		Map<String, Double> totals = new LinkedHashMap<>();
		for (Transaction transaction : successfulPurchases()) {
			Long customerId = customerIdForCard(transaction.getCardNumber());
			String key = customerId + "|" + YearMonth.from(transaction.getTransactionDateTime());
			totals.merge(key, transaction.getAmount(), Double::sum);
		}

		List<Map<String, Object>> response = new ArrayList<>();
		for (Map.Entry<String, Double> entry : totals.entrySet()) {
			String[] parts = entry.getKey().split("\\|");
			Map<String, Object> item = new LinkedHashMap<>();
			item.put("customerId", Long.valueOf(parts[0]));
			item.put("month", parts[1]);
			item.put("totalSpending", entry.getValue());
			response.add(item);
		}
		return response;
	}

	private List<Transaction> successfulPurchases() {
		return successfulTransactions(TransactionType.PURCHASE);
	}

	private List<Transaction> successfulTransactions(TransactionType type) {
		return getAllTransactions().stream()
				.filter(transaction -> transaction.getStatus() == TransactionStatus.SUCCESS)
				.filter(transaction -> transaction.getTransactionType() == type)
				.toList();
	}

	private Map<String, Object> merchantReport(List<Transaction> purchases, String metricName, boolean sumAmounts) {
		Map<Long, Double> totals = new LinkedHashMap<>();
		for (Transaction transaction : purchases) {
			totals.merge(transaction.getMerchantId(), sumAmounts ? transaction.getAmount() : 1.0, Double::sum);
		}
		Map.Entry<Long, Double> result = totals.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.orElseThrow(() -> new IllegalStateException("No successful purchases are available."));

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("merchantId", result.getKey());
		response.put("merchant", restTemplate.getForObject(merchantServiceUrl + "/merchants/" + result.getKey(), Object.class));
		response.put(metricName, sumAmounts ? result.getValue() : result.getValue().longValue());
		return response;
	}

	private Map<String, Object> cardUsageReport(boolean mostUsed) {
		Map<String, Long> usage = new LinkedHashMap<>();
		for (Transaction transaction : successfulPurchases()) {
			usage.merge(transaction.getCardNumber(), 1L, Long::sum);
		}
		Map.Entry<String, Long> result = usage.entrySet().stream()
				.sorted((left, right) -> mostUsed
						? Long.compare(right.getValue(), left.getValue())
						: Long.compare(left.getValue(), right.getValue()))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("No successful purchases are available."));

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("cardNumber", result.getKey());
		response.put("usageCount", result.getValue());
		return response;
	}

	private Map<String, Object> customerAmountReport(TransactionType type, String metricName) {
		Map<Long, Double> totals = new LinkedHashMap<>();
		for (Transaction transaction : successfulTransactions(type)) {
			totals.merge(customerIdForCard(transaction.getCardNumber()), transaction.getAmount(), Double::sum);
		}
		Map.Entry<Long, Double> result = totals.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.orElseThrow(() -> new IllegalStateException("No successful transactions are available."));

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("customerId", result.getKey());
		response.put("customer", restTemplate.getForObject(customerServiceUrl + "/customers/" + result.getKey(), Object.class));
		response.put(metricName, result.getValue());
		return response;
	}

	@SuppressWarnings("unchecked")
	private Long customerIdForCard(String cardNumber) {
		Map<String, Object> card = restTemplate.getForObject(cardServiceUrl + "/cards/" + cardNumber, Map.class);
		if (card == null || card.get("customerId") == null) {
			throw new IllegalStateException("Card Service did not return a customer ID for card " + cardNumber);
		}
		Object customerId = card.get("customerId");
		return customerId instanceof Number number
				? number.longValue()
				: Long.valueOf(customerId.toString());
	}
}
