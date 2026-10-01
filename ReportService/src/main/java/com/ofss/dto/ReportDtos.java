package com.ofss.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public final class ReportDtos {

    private ReportDtos() {
    }

    public record CustomerDto(
            Long customerId,
            String customerName,
            String emailAddress,
            String mobileNumber,
            String panNumber
    ) {
    }

    public record CardDto(
            String cardNumber,
            Long customerId,
            String cardType,
            Double creditLimit,
            Double availableCredit,
            Double outstandingAmount,
            LocalDate expiryDate,
            String status
    ) {
    }

    public record MerchantDto(
            Long merchantId,
            String merchantName,
            String category,
            String location
    ) {
    }

    public record TransactionDto(
            Long transactionId,
            String cardNumber,
            String transactionType,
            Double amount,
            Long merchantId,
            LocalDateTime transactionDateTime,
            String status
    ) {
    }

    public record ReportItem(
            Long id,
            String name,
            BigDecimal amountOrCount
    ) {
    }

    public record CardUsageItem(
            String cardNumber,
            Long usageCount
    ) {
    }

    public record MonthlySpendingItem(
            String month,
            Long customerId,
            String customerName,
            BigDecimal totalSpent
    ) {
    }

    public record GeneratedReport(
            LocalDateTime generatedAt,
            Map<String, Object> reports
    ) {
    }
}