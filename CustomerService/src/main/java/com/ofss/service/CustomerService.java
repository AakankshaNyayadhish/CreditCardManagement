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
		// here i tried to use "programming to interface concept because it provides us more flexibility"
		return customerRepository.findAll();
	}
	
	public Optional<Customer> listCustomerById(Long customerId) {
		return customerRepository.findById(customerId);
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
			existingCustomer.setFirstName(customer.getFirstName());
			existingCustomer.setLastName(customer.getLastName());
			existingCustomer.setPhoneNumber(customer.getPhoneNumber());
			existingCustomer.setEmailId(customer.getEmailId());
			existingCustomer.setDateOfBirth(customer.getDateOfBirth());
			return customerRepository.save(existingCustomer);
		}
		return null;	
	}
	
	public Customer updateCustomerPartiallyById(Long customerId,Customer customer){
		Optional<Customer> optionalCustomer=customerRepository.findById(customerId);
		if(optionalCustomer.isPresent()) {
			Customer existingCustomer=optionalCustomer.get();
//			Customer updatedCustomer=new Customer(customerId,customer.getUserId(),
//					customer.getFirstName(),customer.getLastName(),customer.getPhoneNumber(),
//					customer.getEmailId(),customer.getDateOfBirth());
			if(customer.getFirstName()!=null) {
				existingCustomer.setFirstName(customer.getFirstName());
			}
			if(customer.getLastName()!=null) {
				existingCustomer.setLastName(customer.getLastName());
			}
			if(customer.getPhoneNumber()!=null) {
				existingCustomer.setPhoneNumber(customer.getPhoneNumber());
			}
			if(customer.getEmailId()!=null) {
				existingCustomer.setEmailId(customer.getEmailId());
			}
			if(customer.getDateOfBirth()!=null) {
				existingCustomer.setDateOfBirth(customer.getDateOfBirth());
			}
			return customerRepository.save(existingCustomer);
		}
		return null;
	}
}