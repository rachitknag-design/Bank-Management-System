package com.bank_management_system.bank_project.repository;

import com.bank_management_system.bank_project.entity.Address;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends JpaRepository<Address, Integer> {

	
	@Query("SELECT a FROM Address a WHERE a.bank.bankId = :bankId")
	public Optional<Address> findByBankWithBankId(@Param("bankId") Integer bankId);
}
