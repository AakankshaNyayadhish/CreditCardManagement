package com.ofss.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ofss.service.MerchantService;

@RestController
public class InternalMerchantController {

    private final MerchantService merchantService;

    public InternalMerchantController(
            MerchantService merchantService) {

        this.merchantService = merchantService;
    }

    @GetMapping("/internal/merchants/{merchantId}/exists")
    public ResponseEntity<Boolean> checkMerchantExists(
            @PathVariable Long merchantId) {

        boolean exists =
                merchantService.merchantExists(merchantId);

        return ResponseEntity.ok(exists);
    }
}