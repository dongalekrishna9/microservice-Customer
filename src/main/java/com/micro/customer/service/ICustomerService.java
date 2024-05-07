package com.micro.customer.service;

import com.micro.customer.model.CustomerLoginDTO;
import com.micro.customer.model.CustomerProfileDTO;
import com.micro.customer.model.CustomerRegisterDTO;

public interface ICustomerService {

	boolean registerCustomer(CustomerRegisterDTO customerRegisterDTO);
	
	boolean loginCustomer(CustomerLoginDTO customerLoginDTO);
	
	CustomerProfileDTO readCustomerByPhoneNumber(Long phoneNumber);
}
