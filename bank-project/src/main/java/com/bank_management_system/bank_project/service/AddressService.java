package com.bank_management_system.bank_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank_management_system.bank_project.entity.Address;
import com.bank_management_system.bank_project.exception.InvalidDataException;
import com.bank_management_system.bank_project.exception.ResourceNotFoundException;
import com.bank_management_system.bank_project.repository.AddressRepository;

@Service
public class AddressService {
	
	@Autowired
	private AddressRepository addressRepository;

	public Address getAddressById(Integer addressId) {
		if(addressId==null) {
			throw new InvalidDataException("Address Id is required to find the address.");
		}
		
		Address fetchedAddress = addressRepository.findById(addressId)
				.orElseThrow(()->new ResourceNotFoundException("No Address record found with Id "+addressId));
		
		return fetchedAddress;
	}

	public Address getAddressByBank(Integer bankId) {
		if(bankId == null) {
			throw new InvalidDataException("Bank Id is required to find Address.");
		}
		
		Address fetchedAddress = addressRepository.findByBankWithBankId(bankId)
				.orElseThrow(()->new ResourceNotFoundException("No Address record found with this Bank Id "+bankId));
		
		return fetchedAddress;
	}

}
