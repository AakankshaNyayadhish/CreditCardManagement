package com.ofss.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.ofss.dto.CardStatement;
import com.ofss.dto.ReportDtos.GeneratedReport;
import com.ofss.service.ReportService;

@RestController
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/reports/generate")
    public ResponseEntity<GeneratedReport> generateReport() {

        return ResponseEntity.ok(
                reportService.generateReport()
        );
    }

    @GetMapping("/reports/latest")
    public ResponseEntity<GeneratedReport> getLatestReport() {

        GeneratedReport report =
                reportService.getLatestReport();

        if (report == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/reports/statements/card/{cardNumber}")
    public ResponseEntity<CardStatement> generateCardStatement(
            @PathVariable String cardNumber,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to) {

        if (from.isAfter(to)) {
            return ResponseEntity.badRequest().build();
        }

        CardStatement statement =
                reportService.generateCardStatement(
                        cardNumber,
                        from,
                        to
                );

        return ResponseEntity.ok(statement);
    }
}