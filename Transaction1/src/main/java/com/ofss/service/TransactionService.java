package com.ofss.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ofss.entity.Transaction;
import com.ofss.enums.TransactionStatus;
import com.ofss.enums.TransactionType;
import com.ofss.repository.TransactionRepository;
import com.ofss.dto.CardResponse;

@Service
public class TransactionService {

    private static final double REWARD_AMOUNT = 100.0;
    private static final double CASHBACK_PERCENTAGE = 0.02;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${card.service.url}")
    private String cardServiceUrl;

    @Value("${merchant.service.url}")
    private String merchantServiceUrl;

    public Transaction processPurchase(Transaction transaction) {

        if (transaction.getCardNumber() == null
                || transaction.getMerchantId() == null
                || transaction.getAmount() == null
                || transaction.getAmount() <= 0) {

            throw new IllegalArgumentException(
                    "Card number, merchant ID, and a positive amount are required."
            );
        }

        transaction.setTransactionType(TransactionType.PURCHASE);
        transaction.setTransactionDateTime(LocalDateTime.now());

        String merchantUrl = merchantServiceUrl
                + "/internal/merchants/"
                + transaction.getMerchantId()
                + "/exists";

        try {
            Boolean merchantExists = restTemplate.getForObject(
                    merchantUrl,
                    Boolean.class
            );

            boolean isMerchantValid = false;
            if (merchantExists != null) {
                if (merchantExists.booleanValue()) {
                    isMerchantValid = true;
                }
            }

            if (!isMerchantValid) {
                transaction.setStatus(TransactionStatus.FAILED);
                transaction.setRewardPoints(0L);
                transaction.setCashbackAmount(0.0);

                return transactionRepository.save(transaction);
            }

        } catch (RestClientException exception) {
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setRewardPoints(0L);
            transaction.setCashbackAmount(0.0);

            return transactionRepository.save(transaction);
        }

        String cardUrl = cardServiceUrl
                + "/internal/cards/"
                + transaction.getCardNumber()
                + "/purchase?amount="
                + transaction.getAmount();

        try {
            Boolean purchaseSuccessful = restTemplate.postForObject(
                    cardUrl,
                    null,
                    Boolean.class
            );

            boolean isPurchaseOk = false;
            if (purchaseSuccessful != null) {
                if (purchaseSuccessful.booleanValue()) {
                    isPurchaseOk = true;
                }
            }

            if (isPurchaseOk) {

                transaction.setStatus(TransactionStatus.SUCCESS);

                Long rewardPoints = (long) (
                        transaction.getAmount() / REWARD_AMOUNT
                );

                transaction.setRewardPoints(rewardPoints);

                double cashback =
                        transaction.getAmount()
                                * CASHBACK_PERCENTAGE;

                cashback = Math.round(cashback * 100.0) / 100.0;

                transaction.setCashbackAmount(cashback);

            } else {

                transaction.setStatus(TransactionStatus.FAILED);
                transaction.setRewardPoints(0L);
                transaction.setCashbackAmount(0.0);
            }

        } catch (RestClientException exception) {

            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setRewardPoints(0L);
            transaction.setCashbackAmount(0.0);
        }

        return transactionRepository.save(transaction);
    }

    public Transaction processPayment(Transaction transaction) {

        if (transaction.getCardNumber() == null
                || transaction.getAmount() == null
                || transaction.getAmount() <= 0) {

            throw new IllegalArgumentException(
                    "Card number and a positive payment amount are required."
            );
        }

        transaction.setTransactionType(TransactionType.PAYMENT);
        transaction.setMerchantId(null);
        transaction.setTransactionDateTime(LocalDateTime.now());

        // Payments do not receive reward points or cashback.
        transaction.setRewardPoints(0L);
        transaction.setCashbackAmount(0.0);

        String cardUrl = cardServiceUrl
                + "/internal/cards/"
                + transaction.getCardNumber()
                + "/payment?amount="
                + transaction.getAmount();

        try {
            Boolean paymentSuccessful = restTemplate.postForObject(
                    cardUrl,
                    null,
                    Boolean.class
            );

            boolean isPaymentOk = false;
            if (paymentSuccessful != null) {
                if (paymentSuccessful.booleanValue()) {
                    isPaymentOk = true;
                }
            }

            if (isPaymentOk) {
                transaction.setStatus(TransactionStatus.SUCCESS);
            } else {
                transaction.setStatus(TransactionStatus.FAILED);
            }

        } catch (RestClientException exception) {
            transaction.setStatus(TransactionStatus.FAILED);
        }

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository
                .findAllByOrderByTransactionDateTimeDesc();
    }

    public List<Transaction> getTransactionsByCard(
            String cardNumber) {

        return transactionRepository
                .findByCardNumberOrderByTransactionDateTimeDesc(
                        cardNumber
                );
    }

    public Transaction getTransactionById(Long transactionId) {
        Optional<Transaction> transactionOptional =
                transactionRepository.findById(transactionId);

        if (transactionOptional.isPresent()) {
            return transactionOptional.get();
        } else {
            return null;
        }
    }

    public List<Transaction> searchByDateRange(
            LocalDateTime from,
            LocalDateTime to) {

        return transactionRepository
                .findByTransactionDateTimeBetweenOrderByTransactionDateTimeDesc(
                        from,
                        to
                );
    }

    public List<Transaction> searchByMerchant(
            Long merchantId) {

        return transactionRepository
                .findByMerchantIdOrderByTransactionDateTimeDesc(
                        merchantId
                );
    }

    public List<Transaction> searchByAmount(
            Double minimum,
            Double maximum) {

        return transactionRepository
                .findByAmountBetweenOrderByAmountDesc(
                        minimum,
                        maximum
                );
    }

    public Long getRewardPointsByCard(String cardNumber) {

        List<Transaction> transactions =
                transactionRepository
                        .findByCardNumberOrderByTransactionDateTimeDesc(
                                cardNumber
                        );

        long totalPoints = 0L;

        for (Transaction transaction : transactions) {

            if (transaction.getTransactionType()
                    == TransactionType.PURCHASE
                    && transaction.getStatus()
                    == TransactionStatus.SUCCESS) {

                if (transaction.getRewardPoints() != null) {
                    totalPoints += transaction.getRewardPoints();
                }
            }
        }

        return totalPoints;
    }

    public Double getCashbackByCard(String cardNumber) {

        List<Transaction> transactions =
                transactionRepository
                        .findByCardNumberOrderByTransactionDateTimeDesc(
                                cardNumber
                        );

        double totalCashback = 0.0;

        for (Transaction transaction : transactions) {

            if (transaction.getTransactionType()
                    == TransactionType.PURCHASE
                    && transaction.getStatus()
                    == TransactionStatus.SUCCESS) {

                if (transaction.getCashbackAmount() != null) {
                    totalCashback += transaction.getCashbackAmount();
                }
            }
        }

        return Math.round(totalCashback * 100.0) / 100.0;
    }
    
    public Double calculateLatePaymentPenalty(
            String cardNumber,
            LocalDate dueDate,
            LocalDate paymentDate) {

        CardResponse card = restTemplate.getForObject(
                cardServiceUrl + "/cards/" + cardNumber,
                CardResponse.class
        );

        if (card == null) {
            throw new RuntimeException("Card not found");
        }

        if (!paymentDate.isAfter(dueDate)) {
            return 0.0;
        }

        double penalty = card.getOutstandingAmount() * 0.02;

        return Math.round(penalty * 100.0) / 100.0;
    }
}