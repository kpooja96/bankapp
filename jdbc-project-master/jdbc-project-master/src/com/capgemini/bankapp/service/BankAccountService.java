package com.capgemini.bankapp.service;

import java.sql.SQLException;
import java.util.List;

import com.capgemini.bankapp.exception.AccountNotFoundException;
import com.capgemini.bankapp.exception.LowBalanceException;
import com.capgemini.bankapp.model.BankAccount;

public interface BankAccountService {

	public double checkBalance(long accountId) throws AccountNotFoundException;
	public double withdraw(long accountId , double amount) throws LowBalanceException, AccountNotFoundException;
	public double deposit(long accountId,double amount) throws AccountNotFoundException;
	public boolean deleteBankAccount(long accountId) throws AccountNotFoundException ;
	public boolean addNewBankAccount(BankAccount account);
	public List<BankAccount> displayAllAccounts();
	public double fundTransfer(long fromAccountId,long toAccountId,double amount) throws LowBalanceException, AccountNotFoundException;
	public BankAccount findAccountById(long accountId) throws SQLException, AccountNotFoundException;
	public boolean updateAccountDetails(long accountId, String newAccountHolderName, String newAccountType);
	
}
