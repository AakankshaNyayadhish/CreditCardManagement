package com.ofss.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.ofss.entity.CreditAccount;
import com.ofss.enums.CreditAccountStatus;
import com.ofss.repository.CreditAccountRepository;

@Service
public class CreditAccountService {
	
	@Autowired
	CreditAccountRepository creditRepository;
	
	public CreditAccount createACreditAccount(CreditAccount credit) {
		return creditRepository.save(credit);
	}
	
	public List<CreditAccount> listAllCreditAccounts() {
		return creditRepository.findAll();
	}
	
	public Optional<CreditAccount> listCreditAccountById(Long creditAccountId) {
		return creditRepository.findById(creditAccountId);
	}
	
	public List<CreditAccount> listCreditAccountsByCustomerId(Long customerId) {
		return creditRepository.findByCustomerId(customerId);
	}
	
	public BigDecimal checkAvailableCredit(Long creditAccountId) {
		if(creditRepository.existsById(creditAccountId)) {
			return creditRepository.findById(creditAccountId).get().getAvailableCredit();
		}
		return null;
	}
	
	public CreditAccount updateCreditAccountById(Long creditAccountId,CreditAccount credit) {
		Optional<CreditAccount> optionalCreditAccount=creditRepository.findById(creditAccountId);
		if(optionalCreditAccount.isPresent()) {
			CreditAccount existingCreditAccount=optionalCreditAccount.get();
			existingCreditAccount.setCreditLimit(credit.getCreditLimit());
			existingCreditAccount.setAvailableCredit(credit.getAvailableCredit());
			existingCreditAccount.setOutstandingBalance(credit.getOutstandingBalance());
			existingCreditAccount.setStatus(credit.getStatus());
			return creditRepository.save(existingCreditAccount);
		}
		return null;
	}
	
	public CreditAccount blockCreditAccount(Long creditAccountId) {
		Optional<CreditAccount> optionalCreditAccount=creditRepository.findById(creditAccountId);
		if(optionalCreditAccount.isPresent()) {
			CreditAccount existingCreditAccount=optionalCreditAccount.get();
			existingCreditAccount.setStatus(CreditAccountStatus.BLOCKED);
			return creditRepository.save(existingCreditAccount);
		}
		return null;
	}
	
	public CreditAccount unblockCreditAccount(Long creditAccountId) {
		Optional<CreditAccount> optionalCreditAccount=creditRepository.findById(creditAccountId);
		if(optionalCreditAccount.isPresent()) {
			CreditAccount existingCreditAccount=optionalCreditAccount.get();
			existingCreditAccount.setStatus(CreditAccountStatus.ACTIVE);
			return creditRepository.save(existingCreditAccount);
		}
		return null;
	}
	
	public CreditAccount closeCreditAccount(Long creditAccountId) {
		Optional<CreditAccount> optionalCreditAccount=creditRepository.findById(creditAccountId);
		if(optionalCreditAccount.isPresent()) {
			CreditAccount existingCreditAccount=optionalCreditAccount.get();
			existingCreditAccount.setStatus(CreditAccountStatus.CLOSED);
			return creditRepository.save(existingCreditAccount);
		}
		return null;
	}
	
}
