package com.ofss.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ofss.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

}
