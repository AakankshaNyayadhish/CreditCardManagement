package com.ofss.service;

import com.ofss.entity.Merchant;
import com.ofss.repository.MerchantRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public Merchant addMerchant(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    public List<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }

    public Optional<Merchant> getMerchantById(Long merchantId) {
        return merchantRepository.findById(merchantId);
    }

    public Optional<Merchant> updateMerchant(Long merchantId, Merchant updatedMerchant) {
        Optional<Merchant> existingMerchant = merchantRepository.findById(merchantId);

        if (existingMerchant.isPresent()) {
            Merchant merchant = existingMerchant.get();

            merchant.setMerchantName(updatedMerchant.getMerchantName());
            merchant.setCategory(updatedMerchant.getCategory());
            merchant.setLocation(updatedMerchant.getLocation());

            return Optional.of(merchantRepository.save(merchant));
        }

        return Optional.empty();
    }

    public boolean deleteMerchant(Long merchantId) {
        if (merchantRepository.existsById(merchantId)) {
            merchantRepository.deleteById(merchantId);
            return true;
        }

        return false;
    }

    public boolean merchantExists(Long merchantId) {
        return merchantRepository.existsById(merchantId);
    }
}