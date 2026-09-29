package com.ofss.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ofss.entity.Card;
import com.ofss.enums.CardStatus;
import com.ofss.repository.CardRepository;

@Service
public class CardService {
    
    private static final Logger logger = LoggerFactory.getLogger(CardService.class);

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String CUSTOMER_SERVICE_URL = "http://localhost:8084/internal/customers/";

    @Transactional
    public Card issueCard(Card card) {
        if (card.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID cannot be null when issuing a card.");
        }

        String url = CUSTOMER_SERVICE_URL + card.getCustomerId() + "/exists";
        Boolean customerExists = false;

        try {
            customerExists = restTemplate.getForObject(url, Boolean.class);
        } catch (RestClientException e) {
            logger.error("Failed to connect to Customer Service at URL: {}. Error: {}", url, e.getMessage());
            throw new RuntimeException("Unable to verify customer existence due to Customer Service downtime.");
        }

        if (customerExists == null || !customerExists) {
            logger.warn("Attempted to issue card for non-existent Customer ID: {}", card.getCustomerId());
            throw new RuntimeException("Customer with ID " + card.getCustomerId() + " does not exist. Cannot issue card.");
        }

        // Set default values for new card
        card.setAvailableCredit(card.getCreditLimit());
        card.setOutstandingAmount(0.0);
        if (card.getStatus() == null) {
            card.setStatus(CardStatus.ACTIVE);
        }
        
        Card savedCard = cardRepository.save(card);
        logger.info("Successfully issued card with number: {} for Customer ID: {}", savedCard.getCardNumber(), card.getCustomerId());
        return savedCard;
    }
    
    public List<Card> listAllCards() {
        return cardRepository.findAll();
    }
    
    public Card listCardByNumber(String cardNumber) {
        Optional<Card> optionalCard = cardRepository.findById(cardNumber);
        return optionalCard.orElse(null);
    }
    
    public List<Card> listCardsByCustomerId(Long customerId) {
        return cardRepository.findByCustomerId(customerId);
    }
    
    @Transactional
    public boolean processPurchase(String cardNumber, Double amount) {
        Card card = listCardByNumber(cardNumber);
        if (card == null || card.getStatus() != CardStatus.ACTIVE) {
            return false; 
        }
        if (card.getAvailableCredit() < amount) {
            return false; 
        }

        card.setAvailableCredit(card.getAvailableCredit() - amount);
        card.setOutstandingAmount(card.getOutstandingAmount() + amount);
        cardRepository.save(card);
        return true;
    }

    @Transactional
    public boolean processPayment(String cardNumber, Double amount) {
        Card card = listCardByNumber(cardNumber);
        if (card == null) return false;

        if (amount > card.getOutstandingAmount()) {
            throw new RuntimeException("Payment amount cannot exceed outstanding balance.");
        }

        card.setAvailableCredit(card.getAvailableCredit() + amount);
        card.setOutstandingAmount(card.getOutstandingAmount() - amount);
        cardRepository.save(card);
        return true;
    }

    @Transactional
    public Card blockCardByNumber(String cardNumber) {
        Card card = listCardByNumber(cardNumber);
        if (card != null) {
            card.setStatus(CardStatus.BLOCKED);
            return cardRepository.save(card);
        }
        return null;
    }

    @Transactional
    public Card unblockCardByNumber(String cardNumber) {
        Card card = listCardByNumber(cardNumber);
        if (card != null) {
            card.setStatus(CardStatus.ACTIVE);
            return cardRepository.save(card);
        }
        return null;
    }
}