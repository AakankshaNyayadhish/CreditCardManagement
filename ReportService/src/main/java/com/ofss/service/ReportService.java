package com.ofss.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ofss.dto.CardStatement;
import com.ofss.dto.ReportDtos.CardDto;
import com.ofss.dto.ReportDtos.CardUsageItem;
import com.ofss.dto.ReportDtos.CustomerDto;
import com.ofss.dto.ReportDtos.GeneratedReport;
import com.ofss.dto.ReportDtos.MerchantDto;
import com.ofss.dto.ReportDtos.MonthlySpendingItem;
import com.ofss.dto.ReportDtos.ReportItem;
import com.ofss.dto.ReportDtos.TransactionDto;

@Service
public class ReportService {

    private final RestTemplate restTemplate;

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    @Value("${card.service.url}")
    private String cardServiceUrl;

    @Value("${merchant.service.url}")
    private String merchantServiceUrl;

    @Value("${transaction.service.url}")
    private String transactionServiceUrl;

    private GeneratedReport latestReport;

    public ReportService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GeneratedReport generateReport() {
        List<CustomerDto> customers = getCustomers();
        List<CardDto> cards = getCards();
        List<MerchantDto> merchants = getMerchants();
        List<TransactionDto> transactions = getTransactions();

        Map<Long, CustomerDto> customerById = new HashMap<>();
        for (CustomerDto customer : customers) {
            customerById.put(customer.customerId(), customer);
        }

        Map<Long, MerchantDto> merchantById = new HashMap<>();
        for (MerchantDto merchant : merchants) {
            merchantById.put(merchant.merchantId(), merchant);
        }

        Map<String, CardDto> cardByNumber = new HashMap<>();
        for (CardDto card : cards) {
            cardByNumber.put(card.cardNumber(), card);
        }

        List<TransactionDto> successfulTransactions = new ArrayList<>();
        for (TransactionDto transaction : transactions) {
            if ("SUCCESS".equalsIgnoreCase(transaction.status())) {
                successfulTransactions.add(transaction);
            }
        }

        List<TransactionDto> successfulPurchases = new ArrayList<>();
        List<TransactionDto> successfulPayments = new ArrayList<>();
        for (TransactionDto transaction : successfulTransactions) {
            if ("PURCHASE".equalsIgnoreCase(transaction.transactionType())) {
                successfulPurchases.add(transaction);
            } else if ("PAYMENT".equalsIgnoreCase(transaction.transactionType())) {
                successfulPayments.add(transaction);
            }
        }

        Map<Long, BigDecimal> outstandingByCustomer = new LinkedHashMap<>();
        for (CustomerDto customer : customers) {
            outstandingByCustomer.put(customer.customerId(), BigDecimal.ZERO);
        }

        for (CardDto card : cards) {
            BigDecimal outstanding = money(card.outstandingAmount());
            BigDecimal currentTotal = outstandingByCustomer.get(card.customerId());
            if (currentTotal == null) {
                currentTotal = BigDecimal.ZERO;
            }
            outstandingByCustomer.put(card.customerId(), currentTotal.add(outstanding));
        }

        Map<Long, BigDecimal> merchantSales = new LinkedHashMap<>();
        Map<Long, Long> merchantTransactionCount = new LinkedHashMap<>();

        for (MerchantDto merchant : merchants) {
            merchantSales.put(merchant.merchantId(), BigDecimal.ZERO);
            merchantTransactionCount.put(merchant.merchantId(), 0L);
        }

        for (TransactionDto purchase : successfulPurchases) {
            if (purchase.merchantId() != null) {
                BigDecimal currentSales = merchantSales.get(purchase.merchantId());
                if (currentSales == null) {
                    currentSales = BigDecimal.ZERO;
                }
                merchantSales.put(purchase.merchantId(), currentSales.add(money(purchase.amount())));

                Long currentCount = merchantTransactionCount.get(purchase.merchantId());
                if (currentCount == null) {
                    currentCount = 0L;
                }
                merchantTransactionCount.put(purchase.merchantId(), currentCount + 1L);
            }
        }

        Map<String, Long> cardUsageCount = new LinkedHashMap<>();
        for (CardDto card : cards) {
            cardUsageCount.put(card.cardNumber(), 0L);
        }

        for (TransactionDto purchase : successfulPurchases) {
            Long count = cardUsageCount.get(purchase.cardNumber());
            if (count == null) {
                count = 0L;
            }
            cardUsageCount.put(purchase.cardNumber(), count + 1L);
        }

        Map<Long, BigDecimal> customerSpending = new LinkedHashMap<>();
        Map<Long, BigDecimal> customerPayments = new LinkedHashMap<>();

        for (TransactionDto purchase : successfulPurchases) {
            CardDto card = cardByNumber.get(purchase.cardNumber());
            if (card != null) {
                BigDecimal currentSpend = customerSpending.get(card.customerId());
                if (currentSpend == null) {
                    currentSpend = BigDecimal.ZERO;
                }
                customerSpending.put(card.customerId(), currentSpend.add(money(purchase.amount())));
            }
        }

        for (TransactionDto payment : successfulPayments) {
            CardDto card = cardByNumber.get(payment.cardNumber());
            if (card != null) {
                BigDecimal currentPay = customerPayments.get(card.customerId());
                if (currentPay == null) {
                    currentPay = BigDecimal.ZERO;
                }
                customerPayments.put(card.customerId(), currentPay.add(money(payment.amount())));
            }
        }

        LocalDate today = LocalDate.now();

        BigDecimal totalPurchaseToday = BigDecimal.ZERO;
        for (TransactionDto transaction : successfulPurchases) {
            if (transaction.transactionDateTime() != null) {
                if (transaction.transactionDateTime().toLocalDate().equals(today)) {
                    totalPurchaseToday = totalPurchaseToday.add(money(transaction.amount()));
                }
            }
        }

        BigDecimal totalPaymentToday = BigDecimal.ZERO;
        for (TransactionDto transaction : successfulPayments) {
            if (transaction.transactionDateTime() != null) {
                if (transaction.transactionDateTime().toLocalDate().equals(today)) {
                    totalPaymentToday = totalPaymentToday.add(money(transaction.amount()));
                }
            }
        }

        List<CardDto> blockedCards = new ArrayList<>();
        for (CardDto card : cards) {
            if ("BLOCKED".equalsIgnoreCase(card.status())) {
                blockedCards.add(card);
            }
        }

        List<CardDto> lowCreditCards = new ArrayList<>();
        for (CardDto card : cards) {
            if (card.creditLimit() != null && card.availableCredit() != null) {
                BigDecimal limit = money(card.creditLimit());
                BigDecimal available = money(card.availableCredit());
                BigDecimal twentyPercent = limit.multiply(new BigDecimal("0.20"));
                if (available.compareTo(twentyPercent) < 0) {
                    lowCreditCards.add(card);
                }
            }
        }

        BigDecimal totalOutstandingAmount = BigDecimal.ZERO;
        for (CardDto card : cards) {
            totalOutstandingAmount = totalOutstandingAmount.add(money(card.outstandingAmount()));
        }

        BigDecimal averagePurchaseAmount = BigDecimal.ZERO;
        if (!successfulPurchases.isEmpty()) {
            BigDecimal sumOfPurchases = BigDecimal.ZERO;
            for (TransactionDto purchase : successfulPurchases) {
                sumOfPurchases = sumOfPurchases.add(money(purchase.amount()));
            }
            averagePurchaseAmount = sumOfPurchases.divide(
                    BigDecimal.valueOf(successfulPurchases.size()),
                    2,
                    RoundingMode.HALF_UP
            );
        }

        TransactionDto largestPurchase = null;
        BigDecimal maxAmount = BigDecimal.ZERO;
        for (TransactionDto purchase : successfulPurchases) {
            BigDecimal amount = money(purchase.amount());
            if (largestPurchase == null || amount.compareTo(maxAmount) > 0) {
                maxAmount = amount;
                largestPurchase = purchase;
            }
        }

        List<MonthlySpendingItem> monthlySpending = createMonthlySpending(
                successfulPurchases,
                cardByNumber,
                customerById
        );

        Map<String, Object> reports = new LinkedHashMap<>();

        reports.put("1_allCustomerDetails", customers);
        reports.put("2_allCreditCardDetails", cards);
        reports.put("3_allMerchantDetails", merchants);
        reports.put("4_completeTransactionHistory", transactions);

        reports.put(
                "5_customersWithHighestOutstandingBalance",
                customerLeaders(outstandingByCustomer, customerById, true)
        );
        reports.put(
                "6_customersWithLowestOutstandingBalance",
                customerLeaders(outstandingByCustomer, customerById, false)
        );

        reports.put(
                "7_merchantsWithHighestSalesAmount",
                merchantMoneyLeaders(merchantSales, merchantById, true)
        );
        reports.put(
                "8_merchantsWithHighestTransactionCount",
                merchantCountLeaders(merchantTransactionCount, merchantById, true)
        );

        reports.put("9_mostFrequentlyUsedCreditCard", cardUsageLeaders(cardUsageCount, true));
        reports.put("10_leastFrequentlyUsedCreditCard", cardUsageLeaders(cardUsageCount, false));

        reports.put("11_totalPurchaseAmountToday", totalPurchaseToday);
        reports.put("12_totalPaymentAmountToday", totalPaymentToday);

        reports.put("13_allBlockedCreditCards", blockedCards);
        reports.put("14_cardsBelowTwentyPercentAvailableCredit", lowCreditCards);

        reports.put(
                "15_customerWithHighestSpend",
                customerLeaders(customerSpending, customerById, true)
        );
        reports.put(
                "16_customerWithHighestPayment",
                customerLeaders(customerPayments, customerById, true)
        );

        reports.put("17_totalOutstandingAcrossAllCustomers", totalOutstandingAmount);
        reports.put("18_averagePurchaseTransactionAmount", averagePurchaseAmount);
        reports.put("19_largestPurchaseTransaction", largestPurchase);
        reports.put("20_monthlySpendingSummaryOfEveryCustomer", monthlySpending);

        latestReport = new GeneratedReport(LocalDateTime.now(), reports);
        return latestReport;
    }

    public GeneratedReport getLatestReport() {
        return latestReport;
    }

    private List<CustomerDto> getCustomers() {
        CustomerDto[] response = restTemplate.getForObject(
                customerServiceUrl + "/customers",
                CustomerDto[].class
        );
        if (response == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(response);
    }

    private List<CardDto> getCards() {
        CardDto[] response = restTemplate.getForObject(
                cardServiceUrl + "/cards",
                CardDto[].class
        );
        if (response == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(response);
    }

    private List<MerchantDto> getMerchants() {
        MerchantDto[] response = restTemplate.getForObject(
                merchantServiceUrl + "/merchants",
                MerchantDto[].class
        );
        if (response == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(response);
    }

    private List<TransactionDto> getTransactions() {
        TransactionDto[] response = restTemplate.getForObject(
                transactionServiceUrl + "/transactions",
                TransactionDto[].class
        );
        if (response == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(response);
    }

    private List<ReportItem> customerLeaders(
            Map<Long, BigDecimal> values,
            Map<Long, CustomerDto> customerById,
            boolean highest) {

        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        BigDecimal target = null;
        for (BigDecimal value : values.values()) {
            if (target == null) {
                target = value;
            } else {
                int comparison = value.compareTo(target);
                if (highest) {
                    if (comparison > 0) {
                        target = value;
                    }
                } else {
                    if (comparison < 0) {
                        target = value;
                    }
                }
            }
        }

        List<ReportItem> result = new ArrayList<>();
        for (Map.Entry<Long, BigDecimal> entry : values.entrySet()) {
            if (entry.getValue().compareTo(target) == 0) {
                CustomerDto customer = customerById.get(entry.getKey());
                String customerName;
                if (customer == null) {
                    customerName = "Unknown Customer";
                } else {
                    customerName = customer.customerName();
                }
                result.add(new ReportItem(entry.getKey(), customerName, entry.getValue()));
            }
        }
        return result;
    }

    private List<ReportItem> merchantMoneyLeaders(
            Map<Long, BigDecimal> values,
            Map<Long, MerchantDto> merchantById,
            boolean highest) {

        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        BigDecimal target = null;
        for (BigDecimal value : values.values()) {
            if (target == null) {
                target = value;
            } else {
                int comparison = value.compareTo(target);
                if (highest) {
                    if (comparison > 0) {
                        target = value;
                    }
                } else {
                    if (comparison < 0) {
                        target = value;
                    }
                }
            }
        }

        List<ReportItem> result = new ArrayList<>();
        for (Map.Entry<Long, BigDecimal> entry : values.entrySet()) {
            if (entry.getValue().compareTo(target) == 0) {
                MerchantDto merchant = merchantById.get(entry.getKey());
                String merchantName;
                if (merchant == null) {
                    merchantName = "Unknown Merchant";
                } else {
                    merchantName = merchant.merchantName();
                }
                result.add(new ReportItem(entry.getKey(), merchantName, entry.getValue()));
            }
        }
        return result;
    }

    private List<ReportItem> merchantCountLeaders(
            Map<Long, Long> values,
            Map<Long, MerchantDto> merchantById,
            boolean highest) {

        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        Long target = null;
        for (Long value : values.values()) {
            if (target == null) {
                target = value;
            } else {
                int comparison = value.compareTo(target);
                if (highest) {
                    if (comparison > 0) {
                        target = value;
                    }
                } else {
                    if (comparison < 0) {
                        target = value;
                    }
                }
            }
        }

        List<ReportItem> result = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : values.entrySet()) {
            if (entry.getValue().equals(target)) {
                MerchantDto merchant = merchantById.get(entry.getKey());
                String merchantName;
                if (merchant == null) {
                    merchantName = "Unknown Merchant";
                } else {
                    merchantName = merchant.merchantName();
                }
                result.add(new ReportItem(entry.getKey(), merchantName, BigDecimal.valueOf(entry.getValue())));
            }
        }
        return result;
    }

    private List<CardUsageItem> cardUsageLeaders(
            Map<String, Long> values,
            boolean highest) {

        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        Long target = null;
        for (Long value : values.values()) {
            if (target == null) {
                target = value;
            } else {
                int comparison = value.compareTo(target);
                if (highest) {
                    if (comparison > 0) {
                        target = value;
                    }
                } else {
                    if (comparison < 0) {
                        target = value;
                    }
                }
            }
        }

        List<CardUsageItem> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : values.entrySet()) {
            if (entry.getValue().equals(target)) {
                result.add(new CardUsageItem(entry.getKey(), entry.getValue()));
            }
        }
        return result;
    }

    private List<MonthlySpendingItem> createMonthlySpending(
            List<TransactionDto> purchases,
            Map<String, CardDto> cardByNumber,
            Map<Long, CustomerDto> customerById) {

        Map<String, BigDecimal> totals = new LinkedHashMap<>();

        for (TransactionDto purchase : purchases) {
            if (purchase.transactionDateTime() == null) {
                continue;
            }

            CardDto card = cardByNumber.get(purchase.cardNumber());
            if (card == null) {
                continue;
            }

            String month = YearMonth.from(purchase.transactionDateTime()).toString();
            String key = month + "_" + card.customerId();

            BigDecimal currentTotal = totals.get(key);
            if (currentTotal == null) {
                currentTotal = BigDecimal.ZERO;
            }
            totals.put(key, currentTotal.add(money(purchase.amount())));
        }

        List<MonthlySpendingItem> result = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : totals.entrySet()) {
            String[] parts = entry.getKey().split("_");
            String month = parts[0];
            Long customerId = Long.valueOf(parts[1]);

            CustomerDto customer = customerById.get(customerId);
            String customerName;
            if (customer == null) {
                customerName = "Unknown Customer";
            } else {
                customerName = customer.customerName();
            }

            result.add(new MonthlySpendingItem(
                    month,
                    customerId,
                    customerName,
                    entry.getValue()
            ));
        }

        return result;
    }

    public CardStatement generateCardStatement(
            String cardNumber,
            LocalDate fromDate,
            LocalDate toDate) {

        CardDto card = restTemplate.getForObject(
                cardServiceUrl + "/cards/" + cardNumber,
                CardDto.class
        );

        if (card == null) {
            throw new RuntimeException("Card not found: " + cardNumber);
        }

        CustomerDto customer = restTemplate.getForObject(
                customerServiceUrl + "/customers/" + card.customerId(),
                CustomerDto.class
        );

        TransactionDto[] transactionArray = restTemplate.getForObject(
                transactionServiceUrl + "/transactions/card/" + cardNumber,
                TransactionDto[].class
        );

        List<TransactionDto> allTransactions;
        if (transactionArray == null) {
            allTransactions = Collections.emptyList();
        } else {
            allTransactions = Arrays.asList(transactionArray);
        }

        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(LocalTime.MAX);

        List<TransactionDto> statementTransactions = new ArrayList<>();
        for (TransactionDto transaction : allTransactions) {
            if (transaction.transactionDateTime() != null) {
                boolean notBefore = !transaction.transactionDateTime().isBefore(fromDateTime);
                boolean notAfter = !transaction.transactionDateTime().isAfter(toDateTime);
                if (notBefore && notAfter) {
                    statementTransactions.add(transaction);
                }
            }
        }

        BigDecimal totalPurchases = BigDecimal.ZERO;
        for (TransactionDto transaction : statementTransactions) {
            boolean isPurchase = "PURCHASE".equalsIgnoreCase(transaction.transactionType());
            boolean isSuccess = "SUCCESS".equalsIgnoreCase(transaction.status());
            if (isPurchase && isSuccess) {
                totalPurchases = totalPurchases.add(money(transaction.amount()));
            }
        }

        BigDecimal totalPayments = BigDecimal.ZERO;
        for (TransactionDto transaction : statementTransactions) {
            boolean isPayment = "PAYMENT".equalsIgnoreCase(transaction.transactionType());
            boolean isSuccess = "SUCCESS".equalsIgnoreCase(transaction.status());
            if (isPayment && isSuccess) {
                totalPayments = totalPayments.add(money(transaction.amount()));
            }
        }

        BigDecimal closingOutstanding = money(card.outstandingAmount());
        BigDecimal openingOutstanding = closingOutstanding.subtract(totalPurchases).add(totalPayments);

        return new CardStatement(
                cardNumber,
                customer,
                card,
                fromDate,
                toDate,
                openingOutstanding,
                statementTransactions,
                totalPurchases,
                totalPayments,
                closingOutstanding,
                money(card.availableCredit())
        );
    }

    private BigDecimal money(Double amount) {
        if (amount == null) {
            return BigDecimal.ZERO;
        } else {
            return BigDecimal.valueOf(amount);
        }
    }
}