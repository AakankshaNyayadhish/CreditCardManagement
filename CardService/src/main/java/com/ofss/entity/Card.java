package com.ofss.entity;

import java.time.LocalDate;

import com.ofss.enums.CardStatus;
import com.ofss.enums.CardType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="CREDIT_CARD")
public class Card {
	
	@Id
	@Column(name="CARD_NUMBER",length=16)
	private String cardNumber;
	
	@Column(name="CUSTOMER_ID",nullable=false)
	private Long customerId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="CARD_TYPE")
	private CardType cardType;
	
	@Column(name="CREDIT_LIMIT",nullable = false)
	private Double creditLimit;
	
	@Column(name="AVAILABLE_CREDIT",nullable = false)
	private Double availableCredit;
	
	@Column(name="OUTSTANDING_AMOUNT",nullable = false)
	private Double outstandingAmount;
	
	@Column(name="EXPIRY_DATE")
	private LocalDate expiryDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name="CARD_STATUS")
	private CardStatus status;

	public Card() {
		super();
	}

	public Card(String cardNumber, Long customerId, CardType cardType, Double creditLimit, Double availableCredit,
			Double outstandingAmount, LocalDate expiryDate, CardStatus status) {
		super();
		this.cardNumber = cardNumber;
		this.customerId = customerId;
		this.cardType = cardType;
		this.creditLimit = creditLimit;
		this.availableCredit = availableCredit;
		this.outstandingAmount = outstandingAmount;
		this.expiryDate = expiryDate;
		this.status = status;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public Double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(Double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public Double getAvailableCredit() {
		return availableCredit;
	}

	public void setAvailableCredit(Double availableCredit) {
		this.availableCredit = availableCredit;
	}

	public Double getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(Double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public CardStatus getStatus() {
		return status;
	}

	public void setStatus(CardStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Card [cardNumber=" + cardNumber + ", customerId=" + customerId + ", cardType=" + cardType
				+ ", creditLimit=" + creditLimit + ", availableCredit=" + availableCredit + ", outstandingAmount="
				+ outstandingAmount + ", expiryDate=" + expiryDate + ", status=" + status + "]";
	}
	
	
}
