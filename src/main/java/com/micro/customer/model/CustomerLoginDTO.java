package com.micro.customer.model;

import java.io.Serializable;

public class CustomerLoginDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long phoneNumber;
	private String password;
	
	public Long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
