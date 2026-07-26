package com.bank_management_system.bank_project.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
		return accountService.createAccount(account);
	}
	
	//2. Get All Accounts
	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<Account>>> getAllAccounts() {
		return accountService.getAllAccounts();
	}
	
	//3. Get Account By Id
	@GetMapping("/{accountId}")
	public ResponseEntity<ResponseStructure<Account>> getAccountById(@PathVariable Integer accountId) {
		return accountService.getAccountById(accountId);
	}
	
	//4. Delete Account
	@DeleteMapping("/{accountId}")
	public ResponseEntity<ResponseStructure<Account>> deleteAccountById(@PathVariable Integer accountId) {
		return accountService.deleteAccountById(accountId);
	}
	
	//5. Update AccountType and HolderName (PatchMapping)
	
	//6. Deposit Amount
	@PutMapping("/deposit/{accountId}")
	public ResponseEntity<ResponseStructure<Account>> depositAmount(@PathVariable Integer accountId,
																		@RequestParam BigDecimal amount ) {
		return accountService.depositAmount(accountId, amount);
	}
	
	//7. Withdraw Amount
	@PutMapping("/withdraw/{accountId}")
	public ResponseEntity<ResponseStructure<Account>> withdrawAmount(@PathVariable Integer accountId, 
																		@RequestParam BigDecimal amount) {
		return accountService.withdrawAmount(accountId, amount);
	}
	
	
}
