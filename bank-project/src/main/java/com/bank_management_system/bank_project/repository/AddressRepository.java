package com.bank_management_system.bank_project.repository;

import com.bank_management_system.bank_project.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
