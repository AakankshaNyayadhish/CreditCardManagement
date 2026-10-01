package com.ofss.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.ofss.dto.ReportDtos.CardDto;
import com.ofss.dto.ReportDtos.CustomerDto;
import com.ofss.dto.ReportDtos.TransactionDto;

public record CardStatement(
        String cardNumber,
        CustomerDto customer,
        CardDto card,
        LocalDate fromDate,
        LocalDate toDate,
        BigDecimal openingOutstandingAmount,
        List<TransactionDto> transactions,
        BigDecimal totalPurchases,
        BigDecimal totalPayments,
        BigDecimal closingOutstandingAmount,
        BigDecimal availableCredit
) {
}