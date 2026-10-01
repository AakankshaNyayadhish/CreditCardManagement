package com.ofss.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ofss.entity.Customer;
import com.ofss.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setCustomerName("Arun Kumar");
        customer.setEmailAddress("arun@test.com");
        customer.setMobileNumber("9876500001");
        customer.setPanNumber("ABCDE1234F");
    }

    @Test
    void shouldInsertCustomer() {
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.insertACustomer(customer);

        assertNotNull(result);
        assertEquals("Arun Kumar", result.getCustomerName());
        verify(customerRepository).save(customer);
    }

    @Test
    void shouldFindCustomerById() {
        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        Optional<Customer> result =
                customerService.listCustomerById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getCustomerId());
    }

    @Test
    void shouldReturnFalseWhenCustomerDoesNotExist() {
        when(customerRepository.existsById(99L))
                .thenReturn(false);

        boolean result =
                customerService.deleteCustomerById(99L);

        assertFalse(result);
        verify(customerRepository, never()).deleteById(99L);
    }

    @Test
    void shouldCheckCustomerExists() {
        when(customerRepository.existsById(1L))
                .thenReturn(true);

        assertTrue(customerService.checkCustomerExists(1L));
    }
}