package com.ofss.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="CUSTOMER")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="CUSTOMER_ID")
	private Long customerId;
	
	@Column(name="CUSTOMER_NAME",nullable = false)
	private String customerName;
	
	@Column(name="EMAIL_ADDRESS",unique=true,nullable = false)
	private String emailAddress;
	
	@Column(name="MOBILE_NUMBER",nullable = false)
	private String mobileNumber;
	
	@Column(name="PAN_NUMBER",unique=true,nullable = false)
	private String panNumber;
	
	public Customer() {
		super();
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", customerName=" + customerName + ", emailAddress="
				+ emailAddress + ", mobileNumber=" + mobileNumber + ", panNumber=" + panNumber + "]";
	}
	
}
