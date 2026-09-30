package com.ofss.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ofss.service.ReportGeneratorService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportGeneratorService reportGeneratorService;

    public ReportController(ReportGeneratorService reportGeneratorService) {
        this.reportGeneratorService = reportGeneratorService;
    }

    @PostMapping("/generate")
    public Map<String, Object> generateReport() {
        return reportGeneratorService.generateReport();
    }

    @GetMapping("/latest")
    public Map<String, Object> getLatestReport() {
        return reportGeneratorService.getLatestReport();
    }

    @GetMapping("/customers")
    public Object getAllCustomers() {
        return reportGeneratorService.getReportSection("allCustomers");
    }

    @GetMapping("/cards")
    public Object getAllCards() {
        return reportGeneratorService.getReportSection("allCards");
    }

    @GetMapping("/merchants")
    public Object getAllMerchants() {
        return reportGeneratorService.getReportSection("allMerchants");
    }

    @GetMapping("/transactions")
    public Object getTransactions() {
        return reportGeneratorService.getReportSection("completeTransactionHistory");
    }

    @GetMapping("/customers/highest-outstanding")
    public Object getHighestOutstandingCustomer() {
        return reportGeneratorService.getReportSection("highestOutstandingCustomer");
    }

    @GetMapping("/customers/lowest-outstanding")
    public Object getLowestOutstandingCustomer() {
        return reportGeneratorService.getReportSection("lowestOutstandingCustomer");
    }

    @GetMapping("/cards/blocked")
    public Object getBlockedCards() {
        return reportGeneratorService.getReportSection("blockedCards");
    }

    @GetMapping("/cards/low-credit")
    public Object getLowCreditCards() {
        return reportGeneratorService.getReportSection("lowAvailableCreditCards");
    }

    @GetMapping("/outstanding/total")
    public Object getTotalOutstandingAmount() {
        return reportGeneratorService.getReportSection("totalOutstandingAmount");
    }

    @GetMapping("/merchants/highest-sales")
    public Object getMerchantWithHighestSales() {
        return reportGeneratorService.getReportSection("merchantWithHighestSales");
    }

    @GetMapping("/merchants/most-transactions")
    public Object getMerchantWithMostTransactions() {
        return reportGeneratorService.getReportSection("merchantWithMostTransactions");
    }

    @GetMapping("/cards/most-used")
    public Object getMostUsedCard() {
        return reportGeneratorService.getReportSection("mostFrequentlyUsedCard");
    }

    @GetMapping("/cards/least-used")
    public Object getLeastUsedCard() {
        return reportGeneratorService.getReportSection("leastFrequentlyUsedCard");
    }

    @GetMapping("/purchases/today")
    public Object getTodayPurchaseAmount() {
        return reportGeneratorService.getReportSection("totalPurchaseAmountToday");
    }

    @GetMapping("/payments/today")
    public Object getTodayPaymentAmount() {
        return reportGeneratorService.getReportSection("totalPaymentAmountToday");
    }

    @GetMapping("/customers/highest-spending")
    public Object getHighestSpendingCustomer() {
        return reportGeneratorService.getReportSection("customerWithHighestSpending");
    }

    @GetMapping("/customers/highest-payment")
    public Object getHighestPaymentCustomer() {
        return reportGeneratorService.getReportSection("customerWithHighestPayment");
    }

    @GetMapping("/purchases/average")
    public Object getAveragePurchaseAmount() {
        return reportGeneratorService.getReportSection("averagePurchaseAmount");
    }

    @GetMapping("/purchases/largest")
    public Object getLargestPurchase() {
        return reportGeneratorService.getReportSection("largestPurchaseTransaction");
    }

    @GetMapping("/customers/monthly-spending")
    public Object getMonthlyCustomerSpending() {
        return reportGeneratorService.getReportSection("monthlyCustomerSpending");
    }
}