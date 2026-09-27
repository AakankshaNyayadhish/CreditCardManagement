package com.ofss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ofss.entity.Card;
import com.ofss.entity.CreditAccount;
import com.ofss.service.CardService;


import java.util.List;

@RestController
public class CardController {
	@Autowired 
	CardService cardService;
	
	@PostMapping("/cards")
	public ResponseEntity<Card> issueCard(@RequestBody Card card) {
		Card issuedCard=cardService.issueCard(card);
		return ResponseEntity.status(HttpStatus.CREATED).body(issuedCard);
	}
	
	@GetMapping("/cards")
	public ResponseEntity<List<Card>> listAllCards() {
		return ResponseEntity.status(HttpStatus.OK).body(cardService.listAllCards());
	}
	
	@GetMapping("/cards/{cardId}")
	public ResponseEntity<Card> listCardById(@PathVariable("cardId") Long cardId) {
		Card card=cardService.listCardById(cardId);
		if(card!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(card);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@GetMapping("/cards/customer/{customerId}")
	public ResponseEntity<List<Card>> listAllCardOfCustomer(@PathVariable("customerId") Long customerId) {
		List<Card>cards=cardService.listCardsByCustomerId(customerId);
		
		if(cards!=null && !cards.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(cards);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PutMapping("/cards/{cardId}")
	public ResponseEntity<Card> updateCardDetailsById(@PathVariable("cardId") Long cardId,@RequestBody Card card) {
		Card updatedCard=cardService.updateCardDetailsById(cardId, card);
		if(updatedCard!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(updatedCard);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PatchMapping("/cards/{cardId}/activate")
	public ResponseEntity<Card> activateCardById(@PathVariable("cardId") Long cardId) {
		Card updatedCard=cardService.activateCardById(cardId);
		if(updatedCard!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(updatedCard);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PatchMapping("/cards/{cardId}/block")
	public ResponseEntity<Card> blockCardById(@PathVariable("cardId") Long cardId) {
		Card updatedCard=cardService.blockCardById(cardId);
		if(updatedCard!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(updatedCard);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PatchMapping("/cards/{cardId}/unblock")
	public ResponseEntity<Card> unblockCardById(@PathVariable("cardId") Long cardId) {
		Card updatedCard=cardService.unblockCardById(cardId);
		if(updatedCard!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(updatedCard);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PatchMapping("/cards/{cardId}/cancel")
	public ResponseEntity<Card> cancelCardById(@PathVariable("cardId") Long cardId) {
		Card updatedCard=cardService.cancelCardById(cardId);
		if(updatedCard!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(updatedCard);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	
}
