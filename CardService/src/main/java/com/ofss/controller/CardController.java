package com.ofss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ofss.entity.Card;
import com.ofss.service.CardService;

import java.util.List;
import java.util.Map;

@RestController
public class CardController {
    
    @Autowired 
    CardService cardService;
    
    @PostMapping("/cards")
    public ResponseEntity<Card> issueCard(@RequestBody Card card) {
        Card issuedCard = cardService.issueCard(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(issuedCard);
    }
    
    @GetMapping("/cards")
    public ResponseEntity<List<Card>> listAllCards() {
        return ResponseEntity.status(HttpStatus.OK).body(cardService.listAllCards());
    }
    
    @GetMapping("/cards/{cardNumber}")
    public ResponseEntity<Card> listCardByNumber(@PathVariable("cardNumber") String cardNumber) {
        Card card = cardService.listCardByNumber(cardNumber);
        if(card != null) {
            return ResponseEntity.status(HttpStatus.OK).body(card);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    @GetMapping("/customers/{customerId}/cards")
    public ResponseEntity<List<Card>> listAllCardOfCustomer(@PathVariable("customerId") Long customerId) {
        List<Card> cards = cardService.listCardsByCustomerId(customerId);
        if(cards != null && !cards.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(cards);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/reports/customers/highest-outstanding")
    public ResponseEntity<Map<String, Object>> highestOutstandingCustomer() {
        return ResponseEntity.ok(cardService.getCustomerOutstanding(true));
    }

    @GetMapping("/reports/customers/lowest-outstanding")
    public ResponseEntity<Map<String, Object>> lowestOutstandingCustomer() {
        return ResponseEntity.ok(cardService.getCustomerOutstanding(false));
    }

    @GetMapping("/reports/cards/blocked")
    public ResponseEntity<List<Card>> blockedCards() {
        return ResponseEntity.ok(cardService.getBlockedCards());
    }

    @GetMapping("/reports/cards/low-credit")
    public ResponseEntity<List<Card>> lowCreditCards() {
        return ResponseEntity.ok(cardService.getLowAvailableCreditCards());
    }

    @GetMapping("/reports/outstanding/total")
    public ResponseEntity<Double> totalOutstandingAmount() {
        return ResponseEntity.ok(cardService.getTotalOutstandingAmount());
    }

    // INTERNAL ENDPOINTS CALLED BY TRANSACTION SERVICE
    @PostMapping("/internal/cards/{cardNumber}/purchase")
    public ResponseEntity<Boolean> applyPurchase(@PathVariable String cardNumber, @RequestParam Double amount) {
        boolean success = cardService.processPurchase(cardNumber, amount);
        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    @PostMapping("/internal/cards/{cardNumber}/payment")
    public ResponseEntity<Boolean> applyPayment(@PathVariable String cardNumber, @RequestParam Double amount) {
        boolean success = cardService.processPayment(cardNumber, amount);
        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    @PatchMapping("/cards/{cardNumber}/block")
    public ResponseEntity<Card> blockCard(@PathVariable("cardNumber") String cardNumber) {
        Card updatedCard = cardService.blockCardByNumber(cardNumber);
        if(updatedCard != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedCard);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    @PatchMapping("/cards/{cardNumber}/unblock")
    public ResponseEntity<Card> unblockCard(@PathVariable("cardNumber") String cardNumber) {
        Card updatedCard = cardService.unblockCardByNumber(cardNumber);
        if(updatedCard != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedCard);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
