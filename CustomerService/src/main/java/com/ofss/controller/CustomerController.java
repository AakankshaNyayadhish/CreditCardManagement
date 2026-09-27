package com.ofss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ofss.entity.Customer;
import com.ofss.service.CustomerService;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@PostMapping("/customers")
	public ResponseEntity<Customer> insertACustomer(@RequestBody Customer customer) {
		return ResponseEntity.status(HttpStatus.CREATED).body(customerService.insertACustomer(customer));
				
	}
	
	@GetMapping("/customers")
	public ResponseEntity<List<Customer>>listAllCustomers() {
		return ResponseEntity.status(HttpStatus.OK).body(customerService.listAllCustomers());			
	}
	
	@GetMapping("/customers/id/{id}")
	public ResponseEntity<Customer> listCustomerById(@PathVariable("id") Long customerId) {
		Optional<Customer> customer=customerService.listCustomerById(customerId);
		if(customer.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(customer.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@DeleteMapping("/customers/id/{id}")
	public ResponseEntity<Void> deleteCustomerById(@PathVariable("id") Long customerId) {
		if(customerService.deleteCustomerById(customerId)) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PutMapping("/customers/id/{id}")
	public ResponseEntity<Customer> updateCustomerById(@PathVariable("id") Long customerId,@RequestBody Customer customer) {
		Customer updatedCustomer=customerService.updateCustomerById(customerId,customer);
		if(updatedCustomer!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PatchMapping("/customers/id/{id}")
	public ResponseEntity<Customer> updateCustomerPartiallyById(@PathVariable("id") Long customerId,@RequestBody Customer customer) {
		Customer updatedCustomer=customerService.updateCustomerPartiallyById(customerId,customer);
		if(updatedCustomer!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
