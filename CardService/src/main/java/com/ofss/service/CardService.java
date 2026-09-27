package com.ofss.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ofss.entity.Card;
import com.ofss.enums.CardStatus;
import com.ofss.enums.CardType;
import com.ofss.repository.CardRepository;

@Service
public class CardService {
	@Autowired
	CardRepository cardRepository;
	
	public Card issueCard(Card card) {
		return cardRepository.save(card);
	}
	
	public List<Card> listAllCards() {
		return cardRepository.findAll();
	}
	
	public Card listCardById(Long cardId) {
		Optional<Card> optionalCard=cardRepository.findById(cardId);
		if(optionalCard.isPresent()) {
			return optionalCard.get();
		}
		return null;
	}
	
	public List<Card> listCardsByCustomerId(Long customerId){
		return cardRepository.findByCustomerId(customerId);
	} 
	
	public Card updateCardDetailsById(Long cardId,Card card) {
		Optional<Card> optionalCard=cardRepository.findById(cardId);
		if(optionalCard.isPresent()) {
			Card existingCard=optionalCard.get();
			existingCard.setCardNumber(card.getCardNumber());
			existingCard.setCardType(card.getCardType());
			existingCard.setExpiryDate(card.getExpiryDate());
			existingCard.setStatus(card.getStatus());
			return cardRepository.save(existingCard);
		}
		return null;
	}
	
	public Card activateCardById(Long cardId) {
		Optional<Card> optionalCard=cardRepository.findById(cardId);
		if(optionalCard.isPresent()) {
			Card existingCard=optionalCard.get();
			existingCard.setStatus(CardStatus.ACTIVE);
			return cardRepository.save(existingCard);
		}
		return null;
	}
	
	public Card blockCardById(Long cardId) {
		Optional<Card> optionalCard=cardRepository.findById(cardId);
		if(optionalCard.isPresent()) {
			Card existingCard=optionalCard.get();
			existingCard.setStatus(CardStatus.BLOCKED);
			return cardRepository.save(existingCard);
		}
		return null;
	}
	public Card unblockCardById(Long cardId) {
		Optional<Card> optionalCard=cardRepository.findById(cardId);
		if(optionalCard.isPresent()) {
			Card existingCard=optionalCard.get();
			existingCard.setStatus(CardStatus.ACTIVE);
			return cardRepository.save(existingCard);
		}
		return null;
	}
	public Card cancelCardById(Long cardId) {
		Optional<Card> optionalCard=cardRepository.findById(cardId);
		if(optionalCard.isPresent()) {
			Card existingCard=optionalCard.get();
			existingCard.setStatus(CardStatus.CANCELLED);
			return cardRepository.save(existingCard);
		}
		return null;
	}
}
