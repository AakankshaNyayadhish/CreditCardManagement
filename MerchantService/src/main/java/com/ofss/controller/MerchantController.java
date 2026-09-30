package com.ofss.controller;

import com.ofss.dto.MerchantResponse;
import com.ofss.entity.Merchant;
import com.ofss.service.MerchantService;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

	private final MerchantService merchantService;

	public MerchantController(MerchantService merchantService) {
		this.merchantService = merchantService;
	}

	// POST /merchants
	@PostMapping
	public ResponseEntity<Merchant> addMerchant(@RequestBody Merchant merchant) {

		Merchant savedMerchant = merchantService.addMerchant(merchant);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedMerchant);
	}

	// GET /merchants
	@GetMapping
	public ResponseEntity<List<Merchant>> getAllMerchants() {

		return ResponseEntity.ok(merchantService.getAllMerchants());
	}

	// GET /merchants/{merchantId}
	@GetMapping("/{merchantId}")
	public ResponseEntity<Merchant> getMerchant(@PathVariable Long merchantId) {

		Optional<Merchant> merchant = merchantService.getMerchantById(merchantId);

		if (merchant.isPresent()) {
			return ResponseEntity.ok(merchant.get());
		}

		return ResponseEntity.notFound().build();
	}

	// PUT /merchants/{merchantId}
	@PutMapping("/{merchantId}")
	public ResponseEntity<Merchant> updateMerchant(@PathVariable Long merchantId, @RequestBody Merchant merchant) {

		Optional<Merchant> updatedMerchant = merchantService.updateMerchant(merchantId, merchant);

		if (updatedMerchant.isPresent()) {
			return ResponseEntity.ok(updatedMerchant.get());
		}

		return ResponseEntity.notFound().build();
	}

	// DELETE /merchants/{merchantId}
	@DeleteMapping("/{merchantId}")
	public ResponseEntity<Void> deleteMerchant(@PathVariable Long merchantId) {

		boolean deleted = merchantService.deleteMerchant(merchantId);

		if (deleted) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.notFound().build();
	}
}