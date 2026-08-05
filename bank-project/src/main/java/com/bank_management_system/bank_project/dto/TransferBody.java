package com.bank_management_system.bank_project.dto;

import java.math.BigDecimal;


public class TransferBody {
	
	private Integer senderAccountId;
	
	private Integer receiverAccountId;
	
	private BigDecimal amount;
	
	public Integer getSenderAccountId() {
		return senderAccountId;
	}
	public void setSenderAccountId(Integer senderAccountId) {
		this.senderAccountId = senderAccountId;
	}
	public Integer getReceiverAccountId() {
		return receiverAccountId;
	}
	public void setReceiverAccountId(Integer receiverAccountId) {
		this.receiverAccountId = receiverAccountId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
}
