package com.ofss.dto;

import jakarta.validation.constraints.NotBlank;

public class MerchantRequest {
	@NotBlank
	private String merchantName;

	private String category;

	private String location;

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
