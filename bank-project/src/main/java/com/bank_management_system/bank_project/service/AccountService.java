package com.bank_management_system.bank_project.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank_management_system.bank_project.dto.ResponseStructure;
import com.bank_management_system.bank_project.dto.TransferBody;
import com.bank_management_system.bank_project.entity.Account;
import com.bank_management_system.bank_project.entity.AccountType;
import com.bank_management_system.bank_project.entity.Bank;
import com.bank_management_system.bank_project.exception.DuplicateResourceException;
import com.bank_management_system.bank_project.exception.InsufficientBalanceException;
import com.bank_management_system.bank_project.exception.InsufficientInitialBalanceException;
import com.bank_management_system.bank_project.exception.InvalidDataException;
import com.bank_management_system.bank_project.exception.ResourceNotFoundException;
import com.bank_management_system.bank_project.repository.AccountRepository;
import com.bank_management_system.bank_project.repository.BankRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private BankRepository bankRepository;
	private BigDecimal minimunBalance = new BigDecimal("10000.0");
	
	public ResponseEntity<ResponseStructure<Account>> createAccount(Account account) {
		
		//check id existence
		if(account.getAccountId()!=null) {
			throw new InvalidDataException("Id should not be provided for Account creation.");
		}
		
		//check accountType
		//if accountType is invalid then HttpMessageNotReadableException will be thrown
		if(account.getAccountType() == null) {
			throw new InvalidDataException("AccountType is required. Allowed types: SAVINGS, CURRENT, FIXED_DEPOSIT.");
		}
		
		//check balance for null and minimum Balance
		if(account.getBalance() == null) {
            throw new InvalidDataException("Account balance is required.");
        }
		
		if(account.getAccountType()==AccountType.SAVINGS||account.getAccountType()==AccountType.CURRENT) {
			if(account.getBalance().compareTo(minimunBalance)<0) {
				throw new InsufficientInitialBalanceException("Required Minimum balance of: "+minimunBalance);
			}
		}
		
		//check accountNumber
		if (account.getAccountNumber() == null) {
            throw new InvalidDataException("Account Number is required.");
        }
		
		if(accountRepository.existsByAccountNumber(account.getAccountNumber())) {
			throw new DuplicateResourceException("Account Number "+account.getAccountNumber()+" already exist.");
		}
		
		//check bank association
		if(account.getBank()==null||account.getBank().getBankId()==null) {
			throw new InvalidDataException("A valid Bank ID must be provided to associate with the account.");	
		}
		
		Bank existingBank = bankRepository.findById(account.getBank().getBankId())
				.orElseThrow(()->new ResourceNotFoundException("Bank with ID " + account.getBank().getBankId() +" does not exist."));
		account.setBank(existingBank);
		
		Account createdAccount = accountRepository.save(account);
		
		
		
		ResponseStructure<Account> res = new ResponseStructure<Account>();
		res.setData(createdAccount);
		res.setStatusCode(HttpStatus.CREATED.value());
		res.setMessage("New Account Created Successfully.");
		
		return new ResponseEntity<ResponseStructure<Account>>(res, HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<List<Account>>> getAllAccounts() {
		List<Account> fetchedAccounts = accountRepository.findAll();
		
		if(fetchedAccounts.isEmpty()) {
			throw new ResourceNotFoundException("No Accounts record present in DB.");
		}
		
		ResponseStructure<List<Account>> res = new ResponseStructure<List<Account>>();
		res.setData(fetchedAccounts);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("All accounts fetched successfully.");
		
		return new ResponseEntity<ResponseStructure<List<Account>>>(res, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<Account>> getAccountById(Integer accountId) {
		
		Account fetchedAccount = accountRepository.findById(accountId)
				.orElseThrow(()->new ResourceNotFoundException("No account found with Id "+accountId));
		
		ResponseStructure<Account> res = new ResponseStructure<Account>();
		res.setData(fetchedAccount);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Account with Id "+accountId+" fetched successfully.");
		
		return new ResponseEntity<ResponseStructure<Account>>(res, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<Account>> deleteAccountById(Integer accountId) {
		
		Account fetchedAccount = accountRepository.findById(accountId)
				.orElseThrow(()->new InvalidDataException("Account can't be deleted since account Id "+accountId+" is invalid"));
		
		accountRepository.delete(fetchedAccount);
		
		ResponseStructure<Account> res = new ResponseStructure<Account>();
		res.setData(fetchedAccount);
		res.setStatusCode(HttpStatus.ACCEPTED.value());
		res.setMessage("Account with Id "+accountId+" deleted successfully.");
		
		return new ResponseEntity<ResponseStructure<Account>>(res, HttpStatus.ACCEPTED);
	}

	public ResponseEntity<ResponseStructure<Account>> depositAmount(Integer accountId, BigDecimal amount) {
		if(accountId==null) {
			throw new InvalidDataException("Account Id must be provided to deposit Amount.");
		}
		BigDecimal min = new BigDecimal("1");
		if(amount.compareTo(min)<0) {
			throw new InvalidDataException("Unable to deposit amount since amount less than 1.");
		}
		
		Account targetAccount = accountRepository.findById(accountId)
				.orElseThrow(()-> new ResourceNotFoundException("Account with Account Id "+accountId+" doesn't exist."));
		targetAccount.setBalance(targetAccount.getBalance().add(amount));
		Account updatedAccount = accountRepository.save(targetAccount);
		
		ResponseStructure<Account> res = new ResponseStructure<Account>();
		res.setData(updatedAccount);
		res.setStatusCode(HttpStatus.ACCEPTED.value());
		res.setMessage("Amount "+amount+" deposited to account with Id "+accountId+", avaliable balance : "+updatedAccount.getBalance());
		
		return new ResponseEntity<ResponseStructure<Account>>(res, HttpStatus.ACCEPTED);
	}

	public ResponseEntity<ResponseStructure<Account>> withdrawAmount(Integer accountId, BigDecimal amount) {
		
		if(accountId==null) {
			throw new InvalidDataException("Account Id must be provided to withdraw Amount.");
		}
		
		if(amount==null) {
			throw new InvalidDataException("Unable to withdraw since Withdraw amount can't be null");
		}
		
		BigDecimal min = new BigDecimal("1");
		
		if(amount.compareTo(min)<0) {
			throw new InvalidDataException("Unable to withdraw amount since amount entered is invalid (Enter Atleast 1.00)");
		}
		
		Account targetAccount = accountRepository.findById(accountId)
				.orElseThrow(()->new ResourceNotFoundException("Account with Account Id "+accountId+" doesn't exist." ));
			
		if(amount.compareTo(targetAccount.getBalance())>0) {
			throw new InvalidDataException("Unable to withdraw amount since amount entered is more than avaliable balance.");
		}
		
		BigDecimal remainingBalance = targetAccount.getBalance().subtract(amount);
		
		
		if(targetAccount.getAccountType() == AccountType.CURRENT || targetAccount.getAccountType() == AccountType.SAVINGS) {
			if(remainingBalance.compareTo(minimunBalance)<0) {
				BigDecimal maxWithdrawable = targetAccount.getBalance().subtract(minimunBalance);
				throw new InsufficientBalanceException("Unable to withdraw since remaining balance gets less than mimimum balance, maximum withdrawable amount: "+maxWithdrawable);
			}
		}
		
		targetAccount.setBalance(remainingBalance);
		Account updatedAccount = accountRepository.save(targetAccount);
		
		ResponseStructure<Account> res = new ResponseStructure<Account>();
		res.setData(updatedAccount);
		res.setStatusCode(HttpStatus.ACCEPTED.value());
		res.setMessage("Amount "+amount+" withdrawn from account with Id "+accountId+", available balance: "+updatedAccount.getBalance());

		return new ResponseEntity<ResponseStructure<Account>>(res, HttpStatus.ACCEPTED);
	}

	@Transactional
	public ResponseEntity<ResponseStructure<Account>> transferAmount(Integer senderAccountId, Integer recieverAccountId,
			BigDecimal amount) {
		
		if(senderAccountId==null) {
			throw new InvalidDataException("Sender's Account Id must be provided to withdraw Amount.");
		}
		
		if(recieverAccountId==null) {
			throw new InvalidDataException("Reciever's Account Id must be provided to deposit Amount.");
		}
		
		if(amount==null) {
			throw new InvalidDataException("Unable to transfer since amount can't be null");
		}
		
		if(senderAccountId.equals(recieverAccountId)) {
			throw new InvalidDataException("Unable to tranfer amount since sender and reciver can't be same.");
		}
		
		BigDecimal min = new BigDecimal("1");
		
		if(amount.compareTo(min)<0) {
			throw new InvalidDataException("Unable to withdraw amount since amount entered is invalid (Enter Atleast 1.00)");
		}
		
		Account senderAccount = accountRepository.findById(senderAccountId)
				.orElseThrow(()->new ResourceNotFoundException("Sender's Account Id doesn't exists: enter a valid AccountId"));
		
		Account recieverAccount = accountRepository.findById(recieverAccountId)
				.orElseThrow(()->new ResourceNotFoundException("Reciever's Account Id doesn't exists: enter a valid AccountId"));
		
		if(senderAccount.getBalance().compareTo(amount)<0) {
			throw new InsufficientBalanceException("Unable to tranfer amount since sender's doesn't have sufficient balance.");
		}
		
		BigDecimal remainingBalance = senderAccount.getBalance().subtract(amount);
		
		if(senderAccount.getAccountType()==AccountType.CURRENT || senderAccount.getAccountType()==AccountType.SAVINGS) {
			if(remainingBalance.compareTo(minimunBalance)<0) {
				BigDecimal maxWithdrawable = senderAccount.getBalance().subtract(minimunBalance);
				throw new InsufficientBalanceException("Unable to withdraw since sender's remaining balance gets less than mimimum balance, maximum withdrawable amount: "+maxWithdrawable);
			}
		}
		
		
		senderAccount.setBalance(remainingBalance);
		recieverAccount.setBalance(recieverAccount.getBalance().add(amount));
		
		accountRepository.save(senderAccount);
		Account updatedRecieverAccount = accountRepository.save(recieverAccount);
		
		ResponseStructure<Account> res = new ResponseStructure<Account>();
		
		res.setData(updatedRecieverAccount);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Transferred "+amount+" successfully from Account ID "+senderAccountId +" to Account ID "+recieverAccountId+".");
		
		return new ResponseEntity<ResponseStructure<Account>>(res, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<ResponseStructure<Account>> transferAmount1(TransferBody transferBody) {
		
		if (transferBody == null) {
		    throw new InvalidDataException("Request body cannot be null.");
		}
		
		if(transferBody.getSenderAccountId()==null) {
			throw new InvalidDataException("Sender's Account Id must be provided to withdraw Amount.");
		}
		
		if(transferBody.getReceiverAccountId()==null) {
			throw new InvalidDataException("Reciever's Account Id must be provided to deposit Amount.");
		}
		
		if(transferBody.getAmount()==null) {
			throw new InvalidDataException("Unable to transer since amount can't be null");
		}
		
		if(transferBody.getSenderAccountId().equals(transferBody.getReceiverAccountId())) {
			throw new InvalidDataException("Unable to tranfer amount since sender and reciver can't be same.");
		}
		
		BigDecimal min = new BigDecimal("1");
		
		if(transferBody.getAmount().compareTo(min)<0) {
			throw new InvalidDataException("Unable to withdraw amount since amount entered is invalid (Enter Atleast 1.00)");
		}
		
		Account senderAccount = accountRepository.findById(transferBody.getSenderAccountId())
				.orElseThrow(()->new ResourceNotFoundException("Sender's Account Id doesn't exists: enter a valid AccountId"));
		Account recieverAccount = accountRepository.findById(transferBody.getReceiverAccountId())
				.orElseThrow(()->new ResourceNotFoundException("Reciever's Account Id doesn't exists: enter a valid AccountId"));
		
		if(senderAccount.getBalance().compareTo(transferBody.getAmount())<0) {
			throw new InsufficientBalanceException("Unable to tranfer amount since sender's doesn't have sufficient balance.");
		}
		
		BigDecimal remainingBalance = senderAccount.getBalance().subtract(transferBody.getAmount());
		
		if(senderAccount.getAccountType()==AccountType.CURRENT || senderAccount.getAccountType()==AccountType.SAVINGS) {
			if(remainingBalance.compareTo(minimunBalance)<0) {
				BigDecimal maxWithdrawable = senderAccount.getBalance().subtract(minimunBalance);
				throw new InsufficientBalanceException("Unable to withdraw since sender's remaining balance gets less than mimimum balance, maximum withdrawable amount: "+maxWithdrawable);
			}
		}
		
		
		senderAccount.setBalance(remainingBalance);
		recieverAccount.setBalance(recieverAccount.getBalance().add(transferBody.getAmount()));
		
		accountRepository.save(senderAccount);
		Account updatedRecieverAccount = accountRepository.save(recieverAccount);
		
		ResponseStructure<Account> res = new ResponseStructure<Account>();
		
		res.setData(updatedRecieverAccount);
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Transferred "+transferBody.getAmount()+" successfully from Account ID "+transferBody.getSenderAccountId() +" to Account ID "+transferBody.getReceiverAccountId()+".");
		
		return new ResponseEntity<ResponseStructure<Account>>(res, HttpStatus.OK);
	}

}
