package com.bank_management_system.bank_project.repository;

import com.bank_management_system.bank_project.entity.Account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Integer> {

	public boolean existsByAccountNumber(Long accountNumber);

	@Query("SELECT a FROM Account a WHERE a.bank.bankId = :bankId")
	public List<Account> findByBank_BankId(@Param("bankId") Integer bankId);
}
