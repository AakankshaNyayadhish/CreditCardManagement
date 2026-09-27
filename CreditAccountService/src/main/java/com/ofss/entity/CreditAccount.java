package com.ofss.entity;

import java.math.BigDecimal;

import com.ofss.enums.CreditAccountStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CreditAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long creditAccountId;
	private Long customerId;
	private BigDecimal creditLimit;
	private BigDecimal availableCredit;
	private BigDecimal outstandingBalance;
	private CreditAccountStatus status;
	public CreditAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CreditAccount(Long creditAccountId, Long customerId, BigDecimal creditLimit, BigDecimal availableCredit,
			BigDecimal outstandingBalance, CreditAccountStatus status) {
		super();
		this.creditAccountId = creditAccountId;
		this.customerId = customerId;
		this.creditLimit = creditLimit;
		this.availableCredit = availableCredit;
		this.outstandingBalance = outstandingBalance;
		this.status = status;
	}
	public Long getCreditAccountId() {
		return creditAccountId;
	}
	public void setCreditAccountId(Long creditAccountId) {
		this.creditAccountId = creditAccountId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public BigDecimal getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}
	public BigDecimal getAvailableCredit() {
		return availableCredit;
	}
	public void setAvailableCredit(BigDecimal availableCredit) {
		this.availableCredit = availableCredit;
	}
	public BigDecimal getOutstandingBalance() {
		return outstandingBalance;
	}
	public void setOutstandingBalance(BigDecimal outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}
	public CreditAccountStatus getStatus() {
		return status;
	}
	public void setStatus(CreditAccountStatus status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Credit [creditAccountId=" + creditAccountId + ", customerId=" + customerId + ", creditLimit="
				+ creditLimit + ", availableCredit=" + availableCredit + ", outstandingBalance=" + outstandingBalance
				+ ", status=" + status + "]";
	}
	
}
