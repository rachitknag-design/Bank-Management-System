package com.bank_management_system.bank_project.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank_management_system.bank_project.entity.Account;
import com.bank_management_system.bank_project.entity.AccountType;
import com.bank_management_system.bank_project.entity.Bank;
import com.bank_management_system.bank_project.exception.DuplicateResourceException;
import com.bank_management_system.bank_project.exception.InsufficientInitialBalanceException;
import com.bank_management_system.bank_project.exception.InvalidDataException;
import com.bank_management_system.bank_project.exception.ResourceNotFoundException;
import com.bank_management_system.bank_project.repository.AccountRepository;
import com.bank_management_system.bank_project.repository.BankRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private BankRepository bankRepository;
	private BigDecimal minimunBalance = new BigDecimal("10000.0");
	
	public Account createAccount(Account account) {
		
		//check id existence
		if(account.getAccountId()!=null) {
			throw new InvalidDataException("Id should not be provided for Account creation.");
		}
		
		//check accountType
		//if accountType is invalid then HttpMessageNotReadableException will be thrown
		if(account.getAccountType() == null) {
			throw new InvalidDataException("AccountType is required. Allowed types: SAVINGS, CURRENT, FIXED_DEPOSIT.");
		}
		
		//check balance for null and minimum Balance
		if(account.getBalance() == null) {
            throw new InvalidDataException("Account balance is required.");
        }
		
		if(account.getAccountType()==AccountType.SAVINGS||account.getAccountType()==AccountType.CURRENT) {
			if(account.getBalance().compareTo(minimunBalance)<0) {
				throw new InsufficientInitialBalanceException("Required Minimum balance of: "+minimunBalance);
			}
		}
		
		//check accountNumber
		if (account.getAccountNumber() == null) {
            throw new InvalidDataException("Account Number is required.");
        }
		
		if(accountRepository.existsByAccountNumber(account.getAccountNumber())) {
			throw new DuplicateResourceException("Account Number "+account.getAccountNumber()+" already exist.");
		}
		
		//check bank association
		if(account.getBank()==null||account.getBank().getBankId()==null) {
			throw new InvalidDataException("A valid Bank ID must be provided to associate with the account.");	
		}
		
		Bank existingBank = bankRepository.findById(account.getBank().getBankId())
				.orElseThrow(()->new ResourceNotFoundException("Bank with ID " + account.getBank().getBankId() +" does not exist."));
		account.setBank(existingBank);
		
		return accountRepository.save(account);
	}

}
