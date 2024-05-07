package com.micro.customer.service.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.customer.entity.Customer;
import com.micro.customer.model.CustomerLoginDTO;
import com.micro.customer.model.CustomerProfileDTO;
import com.micro.customer.model.CustomerRegisterDTO;
import com.micro.customer.repository.CustomerRepository;
import com.micro.customer.service.ICustomerService;
@Service
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	CustomerRepository custRepo;
	
	//register a customer
	@Override
	public boolean registerCustomer(CustomerRegisterDTO customerRegisterDTO) {
		
		if(this.custRepo.existsById(customerRegisterDTO.getPhoneNumber()))
		{
			return false;
		}
		else
		{
			Customer customer=new Customer();
			BeanUtils.copyProperties(customerRegisterDTO, customer);
			this.custRepo.save(customer);
			return true;
		}
		
	}

	//Login customer
	@Override
	public boolean loginCustomer(CustomerLoginDTO customerLoginDTO) {
		
		Customer customer = this.custRepo.findByPhoneNumberAndPassword(customerLoginDTO.getPhoneNumber(), customerLoginDTO.getPassword());
		if(customer!=null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	//Fetch customer by phoneNumber(id)
	@Override
	public CustomerProfileDTO readCustomerByPhoneNumber(Long phoneNumber) {
		
		Optional<Customer> optional = this.custRepo.findById(phoneNumber);
		if(optional.isPresent())
		{
			Customer customer = optional.get();
			CustomerProfileDTO customerProfileDTO=new CustomerProfileDTO();
			BeanUtils.copyProperties(customer, customerProfileDTO);
			return customerProfileDTO;
		}
		else
		{
			return null;
		}		
	}

}
