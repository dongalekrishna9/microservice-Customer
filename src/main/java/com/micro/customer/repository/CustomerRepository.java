package com.micro.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.customer.entity.Customer;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Customer findByPhoneNumberAndPassword(Long phoneNumber, String password);
}
