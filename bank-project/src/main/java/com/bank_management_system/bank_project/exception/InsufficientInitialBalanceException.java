package com.bank_management_system.bank_project.exception;

public class InsufficientInitialBalanceException extends RuntimeException {
	public InsufficientInitialBalanceException(String message) {
		super(message);
	}
}
