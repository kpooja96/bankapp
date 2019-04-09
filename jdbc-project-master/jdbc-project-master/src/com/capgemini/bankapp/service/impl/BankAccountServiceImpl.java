package com.capgemini.bankapp.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.capgemini.bankapp.dao.BankAccountDao;
import com.capgemini.bankapp.dao.impl.BankAccountDaoImpl;
import com.capgemini.bankapp.exception.AccountNotFoundException;
import com.capgemini.bankapp.exception.LowBalanceException;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.service.BankAccountService;
import com.capgemini.bankapp.util.DbUtil;

public class BankAccountServiceImpl implements BankAccountService {

	private BankAccountDao bankAccountDao;
	
	Logger logger =Logger.getLogger(DbUtil.class);

	public BankAccountServiceImpl() {
		bankAccountDao = new BankAccountDaoImpl();
	}

	@Override
	public double checkBalance(long accountId) throws AccountNotFoundException {

		double balance =  bankAccountDao.getBalance(accountId);
		if(balance >=0)
			return balance;
		throw new AccountNotFoundException("account doesnt exist");
	}

	@Override
	public double withdraw(long accountId, double amount) throws LowBalanceException, AccountNotFoundException {
		double currentBalance = bankAccountDao.getBalance(accountId);
		
		if(currentBalance < 0)
			throw new AccountNotFoundException("BankAccount doesnt exist");

		if (currentBalance > amount) {
			currentBalance = currentBalance - amount;
			bankAccountDao.updateBalance(accountId, currentBalance);
			DbUtil.commit();
			return currentBalance;
		} else
			
			throw new LowBalanceException("insufficient balance");

	}
	
	
	public double withdrawForFundTransfer(long accountId, double amount) throws LowBalanceException, AccountNotFoundException {
		double currentBalance = bankAccountDao.getBalance(accountId);
		
		if(currentBalance < 0)
			throw new AccountNotFoundException("BankAccount doesnt exist");

		if (currentBalance > amount) {
			currentBalance = currentBalance - amount;
			bankAccountDao.updateBalance(accountId, currentBalance);
			return currentBalance;
		} else
			
			throw new LowBalanceException("insufficient balance");

	}

	@Override
	public double deposit(long accountId, double amount) throws AccountNotFoundException {

		double newBalance = bankAccountDao.getBalance(accountId);
		if(newBalance < 0)
			throw new AccountNotFoundException("account doesn't exist");
		newBalance = bankAccountDao.getBalance(accountId) + amount;
		bankAccountDao.updateBalance(accountId, newBalance);
		DbUtil.commit();
		return newBalance;
	}

	@Override
	public boolean deleteBankAccount(long accountId) throws AccountNotFoundException {
		
		boolean result = bankAccountDao.deleteBankAccount(accountId);
		if(result) {
			DbUtil.commit();
			return result;
		}else
			throw new AccountNotFoundException("Account Doesn't exist");
	}

	@Override
	public boolean addNewBankAccount(BankAccount account) {
		boolean result = bankAccountDao.addNewBankAccount(account);
		if(result == true)
			DbUtil.commit();
		return result;
	}

	@Override
	public List<BankAccount> displayAllAccounts() {
		
		return bankAccountDao.displayAllAccounts();
	}

	@Override
	public double fundTransfer(long fromAccountId, long toAccountId, double amount) throws AccountNotFoundException,LowBalanceException {

		try {
			double newBalance = withdrawForFundTransfer(fromAccountId,amount);
			deposit(toAccountId,amount);
			DbUtil.commit();
			return newBalance;
		} 
		catch (LowBalanceException|AccountNotFoundException e) {
			logger.error("Exception",e);
			DbUtil.rollback();
			throw e;
		}
		}
	

	@Override
	public BankAccount findAccountById(long accountId) throws SQLException, AccountNotFoundException {
		BankAccount account = bankAccountDao.displaySingleAccount(accountId);
		if(bankAccountDao != null)
			return account;
		throw new AccountNotFoundException("account doesn't exist");
		
	}

	@Override
	public boolean updateAccountDetails(long accountId, String newAccountHolderName, String newAccountType) {
		
		boolean result = bankAccountDao.updateAccountDetails(accountId, newAccountHolderName, newAccountType);
		if(result)
			DbUtil.commit();
		return result;
	}

}
