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
}