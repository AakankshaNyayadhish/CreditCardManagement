package com.ofss.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    @Transactional
    public Card issueCard(Card card) {
        // Basic validations first
        if (card.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID cannot be null when issuing a card.");
        }

        if (card.getCreditLimit() == null || card.getCreditLimit() <= 0) {
            throw new IllegalArgumentException("Credit limit must be greater than zero.");
        }

        // Hit customer service to make sure the user is real
        String url = customerServiceUrl + "/internal/customers/" + card.getCustomerId() + "/exists";
        Boolean customerExists;

        try {
            customerExists = restTemplate.getForObject(url, Boolean.class);
        } catch (RestClientException exception) {
            logger.error("Unable to connect to Customer Service: {}", exception.getMessage());
            throw new RuntimeException("Unable to verify customer existence.");
        }

        if (!Boolean.TRUE.equals(customerExists)) {
            throw new RuntimeException("Customer with ID " + card.getCustomerId() + " does not exist.");
        }

        card.setAvailableCredit(card.getCreditLimit());
        card.setOutstandingAmount(0.0);

        if (card.getStatus() == null) {
            card.setStatus(CardStatus.ACTIVE);
        }

        return cardRepository.save(card);
    }

    public List<Card> listAllCards() {
        return cardRepository.findAll();
    }

    public Card listCardByNumber(String cardNumber) {
        Optional<Card> optionalCard = cardRepository.findById(cardNumber);
        return optionalCard.orElse(null);
    }
    
    @Transactional
    public Card updateCard(String cardNumber, Card updatedCard) {
        Card existingCard = listCardByNumber(cardNumber);
        if (existingCard == null) {
            return null;
        }

        if (updatedCard.getCardType() != null) {
            existingCard.setCardType(updatedCard.getCardType());
        }

        if (updatedCard.getExpiryDate() != null) {
            existingCard.setExpiryDate(updatedCard.getExpiryDate());
        }

        return cardRepository.save(existingCard);
    }
    
    public List<Card> listCardsByCustomerId(Long customerId) {
        return cardRepository.findByCustomerId(customerId);
    }

    @Transactional
    public boolean processPurchase(String cardNumber, Double amount) {
        if (amount == null || amount <= 0) {
            return false;
        }

        Card card = listCardByNumber(cardNumber);
        if (card == null || card.getStatus() != CardStatus.ACTIVE) {
            return false;
        }

        // Check if card is expired
        if (card.getExpiryDate() != null && card.getExpiryDate().isBefore(LocalDate.now())) {
            return false;
        }

        if (card.getAvailableCredit() < amount) {
            return false; // not enough limit
        }

        card.setAvailableCredit(card.getAvailableCredit() - amount);
        card.setOutstandingAmount(card.getOutstandingAmount() + amount);

        cardRepository.save(card);
        return true;
    }

    @Transactional
    public boolean processPayment(String cardNumber, Double amount) {
        if (amount == null || amount <= 0) {
            return false;
        }

        Card card = listCardByNumber(cardNumber);
        if (card == null || card.getStatus() != CardStatus.ACTIVE) {
            return false;
        }

        if (amount > card.getOutstandingAmount()) {
            return false;
        }

        card.setAvailableCredit(card.getAvailableCredit() + amount);
        card.setOutstandingAmount(card.getOutstandingAmount() - amount);

        cardRepository.save(card);
        return true;
    }

    @Transactional
    public Card blockCardByNumber(String cardNumber) {
        Card card = listCardByNumber(cardNumber);
        if (card == null) {
            return null;
        }

        card.setStatus(CardStatus.BLOCKED);
        return cardRepository.save(card);
    }

    @Transactional
    public Card unblockCardByNumber(String cardNumber) {
        Card card = listCardByNumber(cardNumber);
        if (card == null) {
            return null;
        }

        card.setStatus(CardStatus.ACTIVE);
        return cardRepository.save(card);
    }
}