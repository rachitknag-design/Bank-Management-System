package com.bank_management_system.bank_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bank_management_system.bank_project.dto.ResponseStructure;
import com.bank_management_system.bank_project.entity.Address;
import com.bank_management_system.bank_project.exception.InvalidDataException;
import com.bank_management_system.bank_project.exception.ResourceNotFoundException;
import com.bank_management_system.bank_project.repository.AddressRepository;

@Service
public class AddressService {
	
	@Autowired
	private AddressRepository addressRepository;

	public ResponseEntity<ResponseStructure<Address>> getAddressById(Integer addressId) {
		if(addressId==null) {
			throw new InvalidDataException("Address Id is required to find the address.");
		}
		
		Address fetchedAddress = addressRepository.findById(addressId)
				.orElseThrow(()->new ResourceNotFoundException("No Address record found with Id "+addressId));
		
		ResponseStructure<Address> res = new ResponseStructure<Address>();
		res.setData(fetchedAddress);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Address with Id "+addressId+" successfully fetched.");
		
		return new ResponseEntity<ResponseStructure<Address>>(res, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<Address>> getAddressByBank(Integer bankId) {
		if(bankId == null) {
			throw new InvalidDataException("Bank Id is required to find Address.");
		}
		
		Address fetchedAddress = addressRepository.findByBankWithBankId(bankId)
				.orElseThrow(()->new ResourceNotFoundException("No Address record found with this Bank Id "+bankId));
		
		ResponseStructure<Address> res = new ResponseStructure<Address>();
		res.setData(fetchedAddress);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Address record fetched with Bank Id "+bankId);
		
		return new ResponseEntity<ResponseStructure<Address>>(res, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Address>>> getAddressByCity(String city) {
		List<Address> fetchedAddresses = addressRepository.findByCity(city);
		
		if(fetchedAddresses.isEmpty()) {
			throw new ResourceNotFoundException("No Address found in the city "+city);
		}
				
		ResponseStructure<List<Address>> res = new ResponseStructure<List<Address>>();
		res.setData(fetchedAddresses);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("All Addresses fetched with city "+city);
		
		return new ResponseEntity<ResponseStructure<List<Address>>>(res, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Address>>> getAddressByCityAndStreet(String city, String street) {
		
		if(city==null ||city.isBlank() || street==null || street.isBlank()) {
			throw new InvalidDataException("Both city and street required to find the address.");
		}
		
		List<Address> fetchedAddresses = addressRepository.findByCityIgnoreCaseAndStreetIgnoreCase(city,street);
		
		if(fetchedAddresses.isEmpty()) {
			throw new ResourceNotFoundException("No Address found at city "+city+" and street "+street);
		}
		
		ResponseStructure<List<Address>> res = new ResponseStructure<List<Address>>();
		res.setData(fetchedAddresses);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("All Address with city "+city+" and state "+street+" fetched successfully.");
		
		return new ResponseEntity<ResponseStructure<List<Address>>>(res, HttpStatus.OK);
	}

	

}
