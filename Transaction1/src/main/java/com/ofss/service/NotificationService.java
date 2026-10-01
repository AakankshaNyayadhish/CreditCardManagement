package com.ofss.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public String sendTransactionEmail(String email,Long transactionId,String status,Double amount) {

        return "Email notification simulated successfully. "
                + "Email: " + email
                + ", Transaction ID: " + transactionId
                + ", Amount: " + amount
                + ", Status: " + status;
    }
}