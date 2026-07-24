package com.bank_management_system.bank_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank_management_system.bank_project.dto.ResponseStructure;
import com.bank_management_system.bank_project.entity.Account;
import com.bank_management_system.bank_project.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	//1. Create Account
	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<Account>> createAccount(@RequestBody Account account) {
		Account createdAccount = accountService.createAccount(account);
		
		ResponseStructure<Account> res = new ResponseStructure<Account>();
		res.setData(createdAccount);
		res.setStatusCode(HttpStatus.CREATED.value());
		res.setMessage("New Account Created Successfully.");
		
		return new ResponseEntity<ResponseStructure<Account>>(res, HttpStatus.CREATED);
	}
}
