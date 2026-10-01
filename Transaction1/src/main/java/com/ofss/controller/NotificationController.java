package com.ofss.controller;

import com.ofss.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendEmailNotification(
            @RequestParam String email,
            @RequestParam Long transactionId,
            @RequestParam String status,
            @RequestParam Double amount) {

        String response = notificationService.sendTransactionEmail(
                email,
                transactionId,
                status,
                amount
        );

        return ResponseEntity.ok(response);
    }
}