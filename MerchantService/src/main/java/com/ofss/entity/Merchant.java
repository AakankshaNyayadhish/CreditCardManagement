package com.ofss.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MERCHANT")
public class Merchant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MERCHANT_ID")
    private Long merchantId;

    @Column(name = "MERCHANT_NAME", nullable = false)
    private String merchantName;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "LOCATION")
    private String location;

    public Merchant() {
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

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