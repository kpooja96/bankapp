package com.capgemini.bankapp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.capgemini.bankapp.exceptions.AccountNotFoundException;
import com.capgemini.bankapp.exceptions.LowBalanceException;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.service.BankAccountService;
import com.capgemini.bankapp.service.impl.BankAccountServiceImpl;

public class BankAccountClient {
	
	static final Logger logger = Logger.getLogger(BankAccountClient.class);


	public static void main(String[] args) throws AccountNotFoundException {

		String accountHolderName;
		int choice;
		String accountType;
		double accountBalance;
		BankAccountService bankService = new BankAccountServiceImpl();
		long accountId;
		double amount;
		long toAccountId;
		
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
			while(true) {
		
		System.out.println("1.Add new bank account \n2.withdraw \n3.deposite\n4.check Balance");
		System.out.println("5.display all accounts\n6.fund transfer\n7.delete an account\n8.search account\n9.Update Account Details\n10.exit");
		System.out.println("Enter your choice");
		choice = Integer.parseInt(reader.readLine());
		
		String newAccountType;
		String newAccountHolderName;
		switch(choice) {
		
		case 1: 
			System.out.println("enter account hoder name");
			accountHolderName = reader.readLine();
			System.out.println("enter account type");
			accountType = reader.readLine();
			System.out.println("Enter initial ammount");
			accountBalance = Double.parseDouble(reader.readLine());
			
			BankAccount account = new BankAccount(accountHolderName, accountType, accountBalance);
			if(bankService.addNewBankAccount(account))
				System.out.println("account created successfully");
			else
				System.out.println("Account creation Failed....");
				break;
				
		case 2:
			System.out.println("Enter your account number");
			accountId = Long.parseLong(reader.readLine());
			System.out.println("Enter ammount you want to withdraw");
			amount = Double.parseDouble(reader.readLine());
			
			try {
				System.out.println("current balance is "+bankService.withdraw(accountId, amount));
			} catch (LowBalanceException e) {
						/* System.out.println(e.getMessage()); */
				logger.error("WITHDRAW FAILED",e);
			} catch (AccountNotFoundException e) {
				logger.error(e.getMessage());
			}
			break;
			
		case 3:
			System.out.println("Enter your account number");
			accountId = Long.parseLong(reader.readLine());
			System.out.println("Enter ammount you want to deposit");
			amount = Double.parseDouble(reader.readLine());
			try {
				System.out.println("current balance is "+bankService.deposit(accountId, amount));
			} catch (AccountNotFoundException e1) {
				logger.error(e1.getMessage());
			}
			
			break;
			
		case 4:
			System.out.println("Enter your account number");
			accountId = Long.parseLong(reader.readLine());
			
			try {
				System.out.println("your account balance is "+bankService.checkBalance(accountId));
			} catch (AccountNotFoundException e1) {
				logger.error(e1.getMessage());
			}
			break;
			
		case 5:
			List<BankAccount> acountss = bankService.displayAllAccounts();
			Iterator<BankAccount> iterator = acountss.iterator();
			
			while(iterator.hasNext()) {
				BankAccount account2 = iterator.next();
				System.out.println("account ID is "+account2.getAccountId());
				System.out.println("account Holder name is "+account2.getAccountHolderName());
				System.out.println("account type is "+account2.getAccountType());
				System.out.println("account balance is "+account2.getAccountBalance());
			}
			break;
		
		case 6:
			System.out.println("Enter sender account number");
			accountId = Long.parseLong(reader.readLine());
			System.out.println("Enter reciever account number");
			toAccountId = Long.parseLong(reader.readLine());
			System.out.println("Enter ammount you want to transfer");
			amount = Double.parseDouble(reader.readLine());
			
			try {
				bankService.fundTransfer(accountId, toAccountId, amount);
				System.out.println("successfully transfered");

				
			} catch (LowBalanceException e) {
				System.out.println(e.getMessage());
			} catch (AccountNotFoundException e) {
				logger.error(e.getMessage());
			}
			
			break;
			
		case 7:
			System.out.println("Enter account number to delete");
			accountId = Long.parseLong(reader.readLine());
			
			bankService.deleteBankAccount(accountId);
			
			System.out.println("successfully deleted");
			break;
			
		case 8:
			System.out.println("enter account id to search");
			accountId = Long.parseLong(reader.readLine());
			
			try {
			
				BankAccount account2 = bankService.findAccountById(accountId);
				System.out.println(account2.getAccountId());
				System.out.println(account2.getAccountHolderName());
				System.out.println(account2.getAccountType());
				System.out.println(account2.getAccountBalance());
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			break;
			
		case 9:
			System.out.println("enter the account ID");
			accountId = Long.parseLong(reader.readLine());
			System.out.println("enter new account hoder name");
			newAccountHolderName = reader.readLine();
			System.out.println("enter new account type");
			newAccountType = reader.readLine();
			
			bankService.updateAccountDetails(accountId, newAccountHolderName, newAccountType);
			
			System.out.println("successfully updated");
			break;
			
		case 10:	
			System.out.println("thanks for banking with us");
			System.exit(0);
		}
		
			}
		
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error("EXCEPTION",e);
			
		}
		
		}
	}
		

