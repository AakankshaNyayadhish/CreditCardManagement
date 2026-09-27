package com.ofss.entity;

import java.time.LocalDate;

import com.ofss.enums.CardStatus;
import com.ofss.enums.CardType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cardId;
	private Long customerId;
	private Long creditAccountId;
	private String cardNumber;
	private CardType cardType;
	private LocalDate expiryDate;
	private CardStatus status;
	public Card() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Card(Long cardId, Long customerId, Long creditAccountId, String cardNumber, CardType cardType,
			LocalDate expiryDate, CardStatus status) {
		super();
		this.cardId = cardId;
		this.customerId = customerId;
		this.creditAccountId = creditAccountId;
		this.cardNumber = cardNumber;
		this.cardType = cardType;
		this.expiryDate = expiryDate;
		this.status = status;
	}
	public Long getCardId() {
		return cardId;
	}
	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getCreditAccountId() {
		return creditAccountId;
	}
	public void setCreditAccountId(Long creditAccountId) {
		this.creditAccountId = creditAccountId;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public CardType getCardType() {
		return cardType;
	}
	public void setCardType(CardType cardType) {
		this.cardType = cardType;
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
		return "Card [cardId=" + cardId + ", customerId=" + customerId + ", creditAccountId=" + creditAccountId
				+ ", cardNumber=" + cardNumber + ", cardType=" + cardType + ", expiryDate=" + expiryDate + ", status="
				+ status + "]";
	}
	
}
