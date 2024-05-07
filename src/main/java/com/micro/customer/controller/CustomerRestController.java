package com.micro.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.micro.customer.model.CustomerLoginDTO;
import com.micro.customer.model.CustomerProfileDTO;
import com.micro.customer.model.CustomerRegisterDTO;
import com.micro.customer.model.PlanDTO;
import com.micro.customer.service.ICustomerService;

@RestController
public class CustomerRestController {
	
	private static String FRIEND_MICRO_URL="http://localhost:7373/FriendDetails/{phoneNumber}";
	private static String PLAN_MICRO_URL="http://localhost:7272/PlanDetails/{planId}";
	
	@Autowired
	ICustomerService custService;
	
	@Autowired
	RestTemplate restTemplate;

	//add/register a customer into database
	@PostMapping(value = "/register", consumes = "application/json")
	public ResponseEntity<Boolean> addCustomer(@RequestBody CustomerRegisterDTO customerRegisterDTO)
	{
		return new ResponseEntity<Boolean>(this.custService.registerCustomer(customerRegisterDTO),HttpStatus.OK);
	}
	
	//Login customer
	@PostMapping(value = "/login" , consumes="application/json")
	public ResponseEntity<Boolean> loginCustomer(@RequestBody CustomerLoginDTO customerLoginDTO)
	{
		return new ResponseEntity<Boolean>(this.custService.loginCustomer(customerLoginDTO),HttpStatus.OK);
	}
	
	//Get all information of customer with given phoneNumber
	@GetMapping(value = "/viewProfile/{phoneNumber}", produces = "application/json")
	public ResponseEntity<CustomerProfileDTO> getAllInfoByPhoneNumber(@PathVariable Long phoneNumber)
	{
		CustomerProfileDTO customerProfileDTO = this.custService.readCustomerByPhoneNumber(phoneNumber);
		//now we have to set currentPlan(PlanDTO) and friendsContactNumbers(List<Long>) into customerProfileDTO.	
		//# To Fetch all the friendsContactNumbers by using phoneNumber, we have to call here Friend-Microservice:
		//********************************************************************************************************
		//Calling Frind-Microservice
		ParameterizedTypeReference<List<Long>> typeRef=new ParameterizedTypeReference<List<Long>>() {};		
		ResponseEntity<List<Long>> exchange = this.restTemplate.exchange(FRIEND_MICRO_URL, HttpMethod.GET, null, typeRef, phoneNumber);
		List<Long> friendsContactNumbers = exchange.getBody();
		
		//set this friendsContactNumbers to customerProfileDTO:
		customerProfileDTO.setFriendsContactNumbers(friendsContactNumbers);
		//*********************************************************************************************************
		
		//#To Fetch PlanDTO by using planId, we have to call here PlanDetails-Microservice:
		//Calling PlanDetails-Microservice		
		PlanDTO planDTO = this.restTemplate.getForObject(PLAN_MICRO_URL, PlanDTO.class, customerProfileDTO.getPlanId());
		
		//set this planDTO to customerProfileDTO:
		customerProfileDTO.setCurrentPlan(planDTO);
		
		return new ResponseEntity<CustomerProfileDTO>(customerProfileDTO,HttpStatus.OK);
	}
}
