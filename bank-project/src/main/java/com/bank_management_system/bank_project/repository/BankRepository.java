package com.bank_management_system.bank_project.repository;

import com.bank_management_system.bank_project.entity.Bank;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {
	
	public boolean existsByIfsc(String ifsc);
	public boolean existsByContactNo(String contactNo);
	
	@Query("SELECT DISTINCT b FROM Bank b LEFT JOIN FETCH b.address")
	public List<Bank> findAllWithAddress();
	
	@Query("SELECT b FROM Bank b LEFT JOIN FETCH b.address WHERE b.bankId = :bankId")
	public Optional<Bank> findByIdWithAddress(@Param("bankId") Integer bankId);
	
	public Optional<Bank> findByIfsc(String ifsc);
	
	@Query("SELECT b FROM Bank b WHERE b.address.addressId = :addressId")
	public Optional<Bank> findByAddressId(@Param("addressId") Integer addressId);

}
