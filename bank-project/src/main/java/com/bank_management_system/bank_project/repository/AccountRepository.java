package com.bank_management_system.bank_project.repository;

import com.bank_management_system.bank_project.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

	public boolean existsByAccountNumber(Long accountNumber);
}
