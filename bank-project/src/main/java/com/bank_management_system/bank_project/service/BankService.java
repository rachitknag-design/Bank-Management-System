package com.bank_management_system.bank_project.service;
import com.bank_management_system.bank_project.entity.Bank;
import com.bank_management_system.bank_project.exception.DuplicateResourceException;
import com.bank_management_system.bank_project.exception.InvalidDataException;
import com.bank_management_system.bank_project.exception.ResourceNotFoundException;
import com.bank_management_system.bank_project.repository.BankRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankService {
	
	@Autowired
	private BankRepository bankRepository;

	
	public Bank createBank(Bank bank) {

	    if (bank.getBankId() != null) {
	        throw new InvalidDataException("Bank Id should not be provided for creation");
	    }
	    
	    if (bankRepository.existsByIfsc(bank.getIfsc())) {
	        throw new DuplicateResourceException("Bank with IFSC "+bank.getIfsc()+" already exists.");
	    }
	    
	    if(bank.getContactNo()!=null && bankRepository.existsByContactNo(bank.getContactNo())) {
	    	throw new DuplicateResourceException("Bank with contact number "+bank.getContactNo()+" already exists");
	    }
	    // Set the bidirectional sync for the child entity
	    if (bank.getAddress() != null) {
	        bank.getAddress().setBank(bank);
	    }
	   
	    return bankRepository.save(bank);
	}


	public List<Bank> getAllBank() {
		return bankRepository.findAllWithAddress();
	}


	public Bank getBankById(Integer bankId) {
		return bankRepository.findById(bankId)
				.orElseThrow(()->new ResourceNotFoundException("Bank record with id "+bankId+" doesn't exists"));
	}

	@Transactional
	public Bank deleteBankById(Integer bankId) {
		
		Bank deletedbank = bankRepository.findById(bankId)
				.orElseThrow(()->new ResourceNotFoundException("Bank record with id "+bankId+" doesn't exists."));
		bankRepository.delete(deletedbank);
		return deletedbank;
	}


	public Bank getBankByIfsc(String ifsc) {
		if(ifsc==null) {
			throw new InvalidDataException("The bank ifsc code is required to fetch.");
		}
		
		Bank fetchedBank = bankRepository.findByIfsc(ifsc)
				.orElseThrow(()->new ResourceNotFoundException("Bank record with ifsc "+ifsc+" doesn't exists."));
		
		return fetchedBank;
	}


	public Bank getBankByAddressId(Integer addressId) {
		if(addressId == null) {
			throw new InvalidDataException("AddressId is mandatory to fetch Bank by addressId.");
		}
		
		return bankRepository.findByAddressId(addressId)
				.orElseThrow(()->new ResourceNotFoundException("Bank record with addressId "+addressId+" doesn't exist."));
	}


	public List<Bank> getBankByCity(String city) {
		List<Bank> fetchedBanksList = bankRepository.findByAddressWithCity(city);
		
		if(fetchedBanksList.isEmpty()) {
			throw new ResourceNotFoundException("No Bank found in this city.");
		}
		
		return fetchedBanksList;
	}


	public Bank getBankByContactNo(String contactNo) {
		Bank fetchedBank = bankRepository.findByContactNo(contactNo)
				.orElseThrow(()->new ResourceNotFoundException("No Bank Record found with contact No "+contactNo));
		
		return fetchedBank;		
	}


//	public Bank updateBankRecord(Bank bank) {
//		
//		if(bank.getBankId()==null) {
//			throw new InvalidDataException("Id must be present to update bank record.");
//		}
//		
//		bankRepository.findById(bank.getBankId())
//		.orElseThrow(()->new ResourceNotFoundException("bank record with id "+bank.getBankName()+" doesn't exists."));
//		
//		return bankRepository.save(bank);
//	}
}
