package com.ofss.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ofss.entity.Customer;
import com.ofss.repository.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	CustomerRepository customerRepository;
	
	public Customer insertACustomer(Customer customer) {
		return customerRepository.save(customer);
	}
	
	public List<Customer> listAllCustomers(){
		return customerRepository.findAll();
	}
	
	public Optional<Customer> listCustomerById(Long customerId) {
		return customerRepository.findById(customerId);
	}
	
	public boolean checkCustomerExists(Long customerId) {
		return customerRepository.existsById(customerId);
	}
	
	public Boolean deleteCustomerById(Long customerId) {
		if(customerRepository.existsById(customerId)) {
			customerRepository.deleteById(customerId);
			return true;
		}
		return false;
	}
	
	public Customer updateCustomerById(Long customerId,Customer customer){
		Optional<Customer> optionalCustomer=customerRepository.findById(customerId);
		if(optionalCustomer.isPresent()) {
			Customer existingCustomer=optionalCustomer.get();
			existingCustomer.setCustomerName(customer.getCustomerName());
			existingCustomer.setEmailAddress(customer.getEmailAddress());
			existingCustomer.setMobileNumber(customer.getMobileNumber());
			existingCustomer.setPanNumber(customer.getPanNumber());
			return customerRepository.save(existingCustomer);
		}
		return null;	
	}
	
	public Customer updateCustomerPartiallyById(Long customerId,Customer customer){
		Optional<Customer> optionalCustomer=customerRepository.findById(customerId);
		if(optionalCustomer.isPresent()) {
			Customer existingCustomer=optionalCustomer.get();
			if(customer.getCustomerName()!=null) {
				existingCustomer.setCustomerName(customer.getCustomerName());
			}
			if(customer.getEmailAddress()!=null) {
				existingCustomer.setEmailAddress(customer.getEmailAddress());
			}
			if(customer.getMobileNumber()!=null) {
				existingCustomer.setMobileNumber(customer.getMobileNumber());
			}
			if(customer.getPanNumber()!=null) {
				existingCustomer.setPanNumber(customer.getPanNumber());
			}
			return customerRepository.save(existingCustomer);
		}
		return null;
	}
}