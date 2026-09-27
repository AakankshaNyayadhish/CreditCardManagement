package com.ofss.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

import com.ofss.entity.CreditAccount;
import com.ofss.service.CreditAccountService;

@RestController
public class CreditAccountController {
	@Autowired
	CreditAccountService creditService;
	
	@PostMapping("/credits")
	public ResponseEntity<CreditAccount> createACreditAccount(@RequestBody CreditAccount credit) {
		return ResponseEntity.status(HttpStatus.CREATED).body(creditService.createACreditAccount(credit));
				
	}
	
	@GetMapping("/credits")
	public ResponseEntity<List<CreditAccount>>listAllCreditAccounts() {
		return ResponseEntity.status(HttpStatus.OK).body(creditService.listAllCreditAccounts());			
	}
	
	@GetMapping("/credits/{creditAccountId}")
	public ResponseEntity<CreditAccount>listCreditAccountById(@PathVariable("creditAccountId") Long creditAccountId) {
		Optional<CreditAccount> optionalCredit=creditService.listCreditAccountById(creditAccountId);
		if(optionalCredit.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(optionalCredit.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();			
	}
	
	@GetMapping("/credits/customer/{customerId}")
	public ResponseEntity<List<CreditAccount>> listCreditAccountByCustomerId(@PathVariable("id") Long customerId) {
		List<CreditAccount>creditAccounts=creditService.listCreditAccountsByCustomerId(customerId);
		
		if(creditAccounts!=null && !creditAccounts.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(creditAccounts);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@GetMapping("/credits/{creditAccountId}/available")
	public ResponseEntity<BigDecimal> checkAvailableCredit(@PathVariable("creditAccountId") Long creditAccountId) {
		BigDecimal availableCredit=creditService.checkAvailableCredit(creditAccountId);
		if(availableCredit!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(availableCredit);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PutMapping("/credits/{creditAccountId}")
	public ResponseEntity<CreditAccount> updateCreditAccountById(@PathVariable("creditAccountId") Long creditAccountId,@RequestBody CreditAccount credit) {
		CreditAccount updatedCreditAccount=creditService.updateCreditAccountById(creditAccountId,credit);
		if(updatedCreditAccount!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(updatedCreditAccount);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PatchMapping("/credits/{creditAccountId}/block")
	public ResponseEntity<CreditAccount> blockCreditAccount(@PathVariable("creditAccountId") Long creditAccountId) {
		CreditAccount updatedCreditAccount=creditService.blockCreditAccount(creditAccountId);
		if(updatedCreditAccount!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(updatedCreditAccount);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PatchMapping("/credits/{creditAccountId}/unblock")
	public ResponseEntity<CreditAccount> unblockCreditAccount(@PathVariable("creditAccountId") Long creditAccountId) {
		CreditAccount updatedCreditAccount=creditService.unblockCreditAccount(creditAccountId);
		if(updatedCreditAccount!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(updatedCreditAccount);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PatchMapping("/credits/{creditAccountId}/close")
	public ResponseEntity<CreditAccount> closeCreditAccount(@PathVariable("creditAccountId") Long creditAccountId) {
		CreditAccount updatedCreditAccount=creditService.closeCreditAccount(creditAccountId);
		if(updatedCreditAccount!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(updatedCreditAccount);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	
}
