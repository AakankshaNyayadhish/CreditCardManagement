package com.ofss.service;

import com.ofss.entity.Merchant;
import com.ofss.repository.MerchantRepository;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    // Add merchant
    public Merchant addMerchant(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    // Get all merchants
    public List<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }

    // Get merchant by ID
    public Optional<Merchant> getMerchantById(Long merchantId) {
        return merchantRepository.findById(merchantId);
    }

    // Update merchant
    public Optional<Merchant> updateMerchant(
            Long merchantId,
            Merchant updatedMerchant) {

        Optional<Merchant> existingMerchant =
                merchantRepository.findById(merchantId);

        if (existingMerchant.isPresent()) {

            Merchant merchant = existingMerchant.get();

            merchant.setMerchantName(updatedMerchant.getMerchantName());
            merchant.setCategory(updatedMerchant.getCategory());
            merchant.setLocation(updatedMerchant.getLocation());

            Merchant savedMerchant = merchantRepository.save(merchant);

            return Optional.of(savedMerchant);
        }

        return null;
    }

    // Delete merchant
    public boolean deleteMerchant(Long merchantId) {

        if (merchantRepository.existsById(merchantId)) {
            merchantRepository.deleteById(merchantId);
            return true;
        }

        return false;
    }

    // Check merchant existence
    public boolean merchantExists(Long merchantId) {
        return merchantRepository.existsById(merchantId);
    }
}