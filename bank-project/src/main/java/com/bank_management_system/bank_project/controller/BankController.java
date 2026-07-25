package com.bank_management_system.bank_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
		return bankService.createBank(bank);	
	}
	
	//2. Get All Bank
	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<Bank>>> getAllBank() {
		return bankService.getAllBank();
	}
	
	//3. Get Bank By Id
	@GetMapping("/{bankId}")
	public ResponseEntity<ResponseStructure<Bank>> getBankById(@PathVariable Integer bankId) {
		return bankService.getBankById(bankId);
	}
	
	//4. Delete Bank
	@DeleteMapping("/{bankId}")
	public ResponseEntity<ResponseStructure<Bank>> deleteBankRecordById(@PathVariable Integer bankId) {
		return bankService.deleteBankById(bankId);
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
		return bankService.getBankByIfsc(ifsc);
	}
	
	
	//8. Get Bank By Address Id
	@GetMapping("/address/{addressId}")
	public ResponseEntity<ResponseStructure<Bank>> getBankByAddressId(@PathVariable Integer addressId) {
		return bankService.getBankByAddressId(addressId);
	}
	
	//9. Get Bank By Address
	
	//10. Get Bank By City
	@GetMapping("/city/{city}")
	public ResponseEntity<ResponseStructure<List<Bank>>> getBankByCity(@PathVariable String city) {
		return bankService.getBankByCity(city);
	}
	
	//11. Get Bank By Contact Number
	@GetMapping("/contact/{contactNo}")
	public ResponseEntity<ResponseStructure<Bank>> getBankByContactNo(@PathVariable String contactNo) {
		return bankService.getBankByContactNo(contactNo);
	}
}
