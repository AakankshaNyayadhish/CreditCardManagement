package com.ofss.service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ofss.config.ServiceUrls;

@Service
public class ReportGeneratorService {

    private final WebClient customerClient;
    private final WebClient cardClient;
    private final WebClient merchantClient;
    private final WebClient transactionClient;

    private final AtomicReference<Map<String, Object>> latestReport =
            new AtomicReference<>();

    public ReportGeneratorService(
            WebClient.Builder webClientBuilder,
            ServiceUrls serviceUrls) {

        this.customerClient = webClientBuilder
                .baseUrl(serviceUrls.getCustomer())
                .build();

        this.cardClient = webClientBuilder
                .baseUrl(serviceUrls.getCard())
                .build();

        this.merchantClient = webClientBuilder
                .baseUrl(serviceUrls.getMerchant())
                .build();

        this.transactionClient = webClientBuilder
                .baseUrl(serviceUrls.getTransaction())
                .build();
    }

    public Map<String, Object> generateReport() {
        Map<String, Object> report = new LinkedHashMap<>();

        report.put("generatedAt", OffsetDateTime.now().toString());
        report.put("reportStatus", "GENERATED");

        report.put("allCustomers", get(customerClient, "/customers"));

        report.put("allCards", get(cardClient, "/cards"));
        report.put("highestOutstandingCustomer",
                get(cardClient, "/reports/customers/highest-outstanding"));
        report.put("lowestOutstandingCustomer",
                get(cardClient, "/reports/customers/lowest-outstanding"));
        report.put("blockedCards", get(cardClient, "/reports/cards/blocked"));
        report.put("lowAvailableCreditCards",
                get(cardClient, "/reports/cards/low-credit"));
        report.put("totalOutstandingAmount",
                get(cardClient, "/reports/outstanding/total"));

        report.put("allMerchants", get(merchantClient, "/merchants"));

        report.put("completeTransactionHistory",
                get(transactionClient, "/transactions"));
        report.put("merchantWithHighestSales",
                get(transactionClient, "/reports/merchants/highest-sales"));
        report.put("merchantWithMostTransactions",
                get(transactionClient, "/reports/merchants/most-transactions"));
        report.put("mostFrequentlyUsedCard",
                get(transactionClient, "/reports/cards/most-used"));
        report.put("leastFrequentlyUsedCard",
                get(transactionClient, "/reports/cards/least-used"));
        report.put("totalPurchaseAmountToday",
                get(transactionClient, "/reports/purchases/today"));
        report.put("totalPaymentAmountToday",
                get(transactionClient, "/reports/payments/today"));
        report.put("customerWithHighestSpending",
                get(transactionClient, "/reports/customers/highest-spending"));
        report.put("customerWithHighestPayment",
                get(transactionClient, "/reports/customers/highest-payment"));
        report.put("averagePurchaseAmount",
                get(transactionClient, "/reports/purchases/average"));
        report.put("largestPurchaseTransaction",
                get(transactionClient, "/reports/purchases/largest"));
        report.put("monthlyCustomerSpending",
                get(transactionClient, "/reports/customers/monthly-spending"));

        latestReport.set(report);
        return report;
    }

    public Map<String, Object> getLatestReport() {
        Map<String, Object> report = latestReport.get();

        if (report == null) {
            throw new IllegalStateException(
                    "No report exists yet. Call POST /reports/generate first.");
        }

        return report;
    }

    public Object getReportSection(String sectionName) {
        Map<String, Object> report = getLatestReport();
        Object section = report.get(sectionName);

        if (section == null) {
            throw new IllegalArgumentException("Requested report section does not exist.");
        }

        return section;
    }

    private Object get(WebClient client, String endpoint) {
        return client.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(Object.class)
                .block(Duration.ofSeconds(10));
    }
}