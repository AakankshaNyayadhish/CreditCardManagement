package com.ofss.dto;

public class MerchantResponse {
	private Long merchantId;
	private String merchantName;
	private String category;
	private String location;

	public MerchantResponse() {
	}

	public MerchantResponse(Long merchantId, String merchantName, String category, String location) {
		this.merchantId = merchantId;
		this.merchantName = merchantName;
		this.category = category;
		this.location = location;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public String getCategory() {
		return category;
	}

	public String getLocation() {
		return location;
	}
}
