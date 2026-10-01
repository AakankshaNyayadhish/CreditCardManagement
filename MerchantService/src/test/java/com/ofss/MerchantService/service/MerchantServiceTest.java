package com.ofss.MerchantService.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ofss.entity.Merchant;
import com.ofss.repository.MerchantRepository;
import com.ofss.service.MerchantService;

@ExtendWith(MockitoExtension.class)
class MerchantServiceTest {

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private MerchantService merchantService;

    @Test
    void shouldAddMerchant() {
        Merchant merchant = new Merchant();
        merchant.setMerchantName("ABC Store");
        merchant.setCategory("Shopping");
        merchant.setLocation("Chennai");

        when(merchantRepository.save(merchant))
                .thenReturn(merchant);

        Merchant result =
                merchantService.addMerchant(merchant);

        assertEquals("ABC Store", result.getMerchantName());
        verify(merchantRepository).save(merchant);
    }

    @Test
    void shouldFindMerchant() {
        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);

        when(merchantRepository.findById(1L))
                .thenReturn(Optional.of(merchant));

        Optional<Merchant> result =
                merchantService.getMerchantById(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnFalseWhenMerchantDoesNotExist() {
        when(merchantRepository.existsById(99L))
                .thenReturn(false);

        assertFalse(merchantService.deleteMerchant(99L));
    }

    @Test
    void shouldCheckMerchantExists() {
        when(merchantRepository.existsById(1L))
                .thenReturn(true);

        assertTrue(merchantService.merchantExists(1L));
    }
}
