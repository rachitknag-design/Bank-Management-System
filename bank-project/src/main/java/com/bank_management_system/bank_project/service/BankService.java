package com.bank_management_system.bank_project.service;
import com.bank_management_system.bank_project.dto.ResponseStructure;
import com.bank_management_system.bank_project.entity.Bank;
import com.bank_management_system.bank_project.exception.DuplicateResourceException;
import com.bank_management_system.bank_project.exception.InvalidDataException;
import com.bank_management_system.bank_project.exception.ResourceNotFoundException;
import com.bank_management_system.bank_project.repository.BankRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankService {
	
	@Autowired
	private BankRepository bankRepository;

	
	public ResponseEntity<ResponseStructure<Bank>> createBank(Bank bank) {

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
	 
	    Bank savedBank = bankRepository.save(bank);
	    
	    ResponseStructure<Bank> res = new ResponseStructure<>();
	    res.setData(savedBank);
	    res.setStatusCode(HttpStatus.CREATED.value());
	    res.setMessage("Bank created successfully!");
	    
	    return new ResponseEntity<ResponseStructure<Bank>>(res, HttpStatus.CREATED);
	}


	public ResponseEntity<ResponseStructure<List<Bank>>> getAllBank() {
		
		List<Bank> fetchedBanks = bankRepository.findAllWithAddress();
		
		ResponseStructure<List<Bank>> res = new ResponseStructure<List<Bank>>();
		res.setData(fetchedBanks);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("All Bank Records fetched.");
		
		return new ResponseEntity<ResponseStructure<List<Bank>>>(res, HttpStatus.OK);
	}


	public ResponseEntity<ResponseStructure<Bank>> getBankById(Integer bankId) {
		Bank fetchBank = bankRepository.findById(bankId)
				.orElseThrow(()->new ResourceNotFoundException("Bank record with id "+bankId+" doesn't exists"));
		
		ResponseStructure<Bank> res = new ResponseStructure<Bank>();
		res.setData(fetchBank);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Bank record fetched.");
		
		return new ResponseEntity<ResponseStructure<Bank>>(res, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<ResponseStructure<Bank>> deleteBankById(Integer bankId) {
		
		Bank deletedBank = bankRepository.findById(bankId)
				.orElseThrow(()->new ResourceNotFoundException("Bank record with id "+bankId+" doesn't exists."));
		bankRepository.delete(deletedBank);
		
		ResponseStructure<Bank> res = new ResponseStructure<Bank>();
		res.setData(deletedBank);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Bank record with id "+bankId+" deleted successfully.");
		
		return new ResponseEntity<ResponseStructure<Bank>>(res,HttpStatus.OK);
	}


	public ResponseEntity<ResponseStructure<Bank>> getBankByIfsc(String ifsc) {
		if(ifsc==null) {
			throw new InvalidDataException("The bank ifsc code is required to fetch.");
		}
		
		Bank fetchedBank = bankRepository.findByIfsc(ifsc)
				.orElseThrow(()->new ResourceNotFoundException("Bank record with ifsc "+ifsc+" doesn't exists."));
	
		ResponseStructure<Bank> res = new ResponseStructure<Bank>();
		res.setData(fetchedBank);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Bank recorded fetched successfully.");
		
		return new ResponseEntity<ResponseStructure<Bank>>(res, HttpStatus.OK);
	}


	public ResponseEntity<ResponseStructure<Bank>> getBankByAddressId(Integer addressId) {
		if(addressId == null) {
			throw new InvalidDataException("AddressId is mandatory to fetch Bank by addressId.");
		}
		
		Bank fetchedBank = bankRepository.findByAddressId(addressId)
				.orElseThrow(()->new ResourceNotFoundException("Bank record with addressId "+addressId+" doesn't exist."));
	
		ResponseStructure<Bank> res = new ResponseStructure<Bank>();
		res.setData(fetchedBank);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Bank record fetched using addressId: "+addressId);
		
		return new ResponseEntity<ResponseStructure<Bank>>(res, HttpStatus.OK);
	}


	public ResponseEntity<ResponseStructure<List<Bank>>> getBankByCity(String city) {
		List<Bank> fetchedBanksList = bankRepository.findByAddressWithCity(city);
		
		if(fetchedBanksList.isEmpty()) {
			throw new ResourceNotFoundException("No Bank found in this city.");
		}
				
		ResponseStructure<List<Bank>> res = new ResponseStructure<List<Bank>>();
		res.setData(fetchedBanksList);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Bank records fetched in city "+city);
		
		return new ResponseEntity<ResponseStructure<List<Bank>>>(res, HttpStatus.OK);
	}


	public ResponseEntity<ResponseStructure<Bank>> getBankByContactNo(String contactNo) {
		Bank fetchedBank = bankRepository.findByContactNo(contactNo)
				.orElseThrow(()->new ResourceNotFoundException("No Bank Record found with contact No "+contactNo));
		
		ResponseStructure<Bank> res = new ResponseStructure<Bank>();
		res.setData(fetchedBank);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Bank with contact Number "+contactNo+" fetched successfully.");
		
		return new ResponseEntity<ResponseStructure<Bank>>(res, HttpStatus.OK);
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
