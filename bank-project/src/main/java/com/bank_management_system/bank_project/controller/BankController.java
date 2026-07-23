package com.bank_management_system.bank_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank_management_system.bank_project.dto.ResponseStructure;
import com.bank_management_system.bank_project.entity.Bank;
import com.bank_management_system.bank_project.service.BankService;

@RestController
@RequestMapping("/bank")
public class BankController {

	@Autowired
	private BankService bankService;
	
	//1. Create Bank
	@PostMapping
	public ResponseEntity<ResponseStructure<Bank>> createBank(@RequestBody Bank bank) {
		Bank savedBank = bankService.createBank(bank);
	    
	    ResponseStructure<Bank> res = new ResponseStructure<>();
	    res.setData(savedBank);
	    res.setStatusCode(HttpStatus.CREATED.value());
	    res.setMessage("Bank created successfully!");
		
		return new ResponseEntity<ResponseStructure<Bank>>(res, HttpStatus.CREATED);
	}
	
	//2. Get All Bank
	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<Bank>>> getAllBank() {
		List<Bank> fetchedBanks = bankService.getAllBank();
		
		ResponseStructure<List<Bank>> res = new ResponseStructure<List<Bank>>();
		res.setData(fetchedBanks);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("All Bank Records fetched.");
		
		return new ResponseEntity<ResponseStructure<List<Bank>>>(res, HttpStatus.OK);
	}
	
	//3. Get Bank By Id
	@GetMapping("/{bankId}")
	public ResponseEntity<ResponseStructure<Bank>> getBankById(@PathVariable Integer bankId) {
		Bank fetchBank = bankService.getBankById(bankId);
		
		ResponseStructure<Bank> res = new ResponseStructure<Bank>();
		res.setData(fetchBank);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Bank record fetched.");
		
		return new ResponseEntity<ResponseStructure<Bank>>(res, HttpStatus.OK);
		
	}
	
	//4. Delete Bank
	@DeleteMapping("/{bankId}")
	public ResponseEntity<ResponseStructure<Bank>> deleteBankRecordById(@PathVariable Integer bankId) {
		Bank deletedBank = bankService.deleteBankById(bankId);
		
		ResponseStructure<Bank> res = new ResponseStructure<Bank>();
		res.setData(deletedBank);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Bank record with id "+bankId+" deleted successfully.");
		
		return new ResponseEntity<ResponseStructure<Bank>>(res,HttpStatus.OK);
	}
	
	//5.1 Update Bank -> PutMapping
//	@PutMapping("/update")
//	public ResponseEntity<ResponseStructure<Bank>> updateBankById(@RequestBody Bank bank) {
//		Bank updatedBank = bankService.updateBankRecord(bank);
//		
//		ResponseStructure<Bank> res = new ResponseStructure<Bank>();
//		res.setData(updatedBank);
//		res.setStatusCode(HttpStatus.ACCEPTED.value());
//		res.setMessage("Bank record with id "+bank.getBankId()+" updated successfully.");
//		
//		return new ResponseEntity<ResponseStructure<Bank>>(res,HttpStatus.ACCEPTED);
//	}
	
	//7. Get By IFSC
	@GetMapping("/ifsc/{ifsc}")
	public ResponseEntity<ResponseStructure<Bank>> getBankByIfsc(@PathVariable String ifsc) {
		Bank fetchedBank = bankService.getBankByIfsc(ifsc);
		
		ResponseStructure<Bank> res = new ResponseStructure<Bank>();
		res.setData(fetchedBank);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Bank recorded fetched successfully.");
		
		return new ResponseEntity<ResponseStructure<Bank>>(res, HttpStatus.OK);
	}
	
	
	//8. Get Bank By Address Id
	@GetMapping("/address/{addressId}")
	public ResponseEntity<ResponseStructure<Bank>> getBankByAddressId(@PathVariable Integer addressId) {
		Bank fetchedBank = bankService.getBankByAddressId(addressId);
		
		ResponseStructure<Bank> res = new ResponseStructure<Bank>();
		res.setData(fetchedBank);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Bank record fetched using addressId: "+addressId);
		
		return new ResponseEntity<ResponseStructure<Bank>>(res, HttpStatus.OK);
	}
	
	//9. Get Bank By Address
	
	//10. Get Bank By City
	@GetMapping("/city/{city}")
	public ResponseEntity<ResponseStructure<List<Bank>>> getBankByCity(@PathVariable String city) {
		List<Bank> fetchedBanksList = bankService.getBankByCity(city);
		
		ResponseStructure<List<Bank>> res = new ResponseStructure<List<Bank>>();
		res.setData(fetchedBanksList);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Bank records fetched in city "+city);
		
		return new ResponseEntity<ResponseStructure<List<Bank>>>(res, HttpStatus.OK);
	}
	
	//11. Get Bank By Contact Number
	@GetMapping("/contact/{contactNo}")
	public ResponseEntity<ResponseStructure<Bank>> getBankByContactNo(@PathVariable String contactNo) {
		Bank fetchedBank = bankService.getBankByContactNo(contactNo);
		
		ResponseStructure<Bank> res = new ResponseStructure<Bank>();
		res.setData(fetchedBank);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Bank with contact Number "+contactNo+" fetched successfully.");
		
		return new ResponseEntity<ResponseStructure<Bank>>(res, HttpStatus.OK);
	}
}
