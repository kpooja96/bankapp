package com.capgemini.bankapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.capgemini.bankapp.exceptions.AccountNotFoundException;
import com.capgemini.bankapp.exceptions.LowBalanceException;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.service.BankAccountService;
import com.capgemini.bankapp.service.impl.BankAccountServiceImpl;

@WebServlet(urlPatterns = { "*.do" }, loadOnStartup = 1)
public class BankAccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BankAccountService bankService;

	public BankAccountController() {
		bankService = new BankAccountServiceImpl();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		String path = request.getServletPath();

		if (path.equals("/getAllBankAccounts.do")) {

			List<BankAccount> bankAccounts = bankService.displayAllAccounts();
			RequestDispatcher dispatcher = request.getRequestDispatcher("Display Account.jsp");

			request.setAttribute("accounts", bankAccounts);
			dispatcher.forward(request, response);

		}

		

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String path = request.getServletPath();
		System.out.println(path);

		if (path.equals("/addNewBankAccount.do")) {
			String accountHolderName = request.getParameter("customer_name");
			String accountType = request.getParameter("account_type");
			double balance = Double.parseDouble(request.getParameter("balance"));

			BankAccount account = new BankAccount(accountHolderName, accountType, balance);
			if (bankService.addNewBankAccount(account)) {
				out.println("<h2> Bank Account created succeessfully..</h2>");
				out.println("<h2><a href='index.html'>|home|</h2>");
				out.close();
			}

		}

		if (path.equals("/withdraw.do")) {
			long accountId = Long.parseLong(request.getParameter("account_name"));
			double amount = Double.parseDouble(request.getParameter("amount"));

			try {
				double balance = bankService.withdraw(accountId, amount);
				out.println("withdraw successfull..current balance is " + balance);
				out.println("<h2><a href='index.html'>|home|</h2>");
				out.close();

			} catch (LowBalanceException e) {
				out.print(e.getMessage());
			} catch (AccountNotFoundException e) {
				out.print(e.getMessage());

			}
		}

		if (path.equals("/deposit.do")) {
			long accountId = Long.parseLong(request.getParameter("account_number"));
			double amount = Double.parseDouble(request.getParameter("amount"));

			try {
				double balance = bankService.deposit(accountId, amount);
				out.println("deposit successfull..current balance is " + balance);
				out.println("<h2><a href='index.html'>|home|</h2>");
				out.close();

			} catch (AccountNotFoundException e) {
				out.print(e.getMessage());
			}
		}

		if (path.equals("/fundtransfer.do")) {
			long from_account = Long.parseLong(request.getParameter("from_account"));
			long to_account = Long.parseLong(request.getParameter("to_account"));
			double amount = Double.parseDouble(request.getParameter("amount"));
			try {
				double balance = bankService.fundTransfer(from_account, to_account, amount);
				out.println("Transfer successfull..current balance is " + balance);
				out.println("<h2><a href='index.html'>|home|</h2>");

			} catch (LowBalanceException e) {
				out.println("<h2>Low balance");
			} catch (AccountNotFoundException e) {
				out.println("<h2>Account not found");
			}
		}

		if (path.equals("/delete.do")) {
			long accountId = Long.parseLong(request.getParameter("account_number"));
			try {
				bankService.deleteBankAccount(accountId);
				out.println("delete successfull..");
				out.println("<h2><a href='index.html'>|home|</h2>");
				out.close();
			} catch (AccountNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (path.equals("/search.do")) {
			long accountId = Long.parseLong(request.getParameter("account_number"));
			BankAccount bankAccounts;
			try {
				bankAccounts = bankService.findAccountById(accountId);
				RequestDispatcher dispatcher = request.getRequestDispatcher("searchAccount.jsp");

				request.setAttribute("accounts", bankAccounts);
				dispatcher.forward(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AccountNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if (path.equals("/update.do")) {
			long accountId = Long.parseLong(request.getParameter("account_number"));
			BankAccount bankAccounts;
			try {
				bankAccounts=bankService.findAccountById(accountId);
				RequestDispatcher dispatcher = request.getRequestDispatcher("infoBankAccount.jsp");
				request.setAttribute("account", bankAccounts);
				dispatcher.forward(request, response);
				
			} catch (AccountNotFoundException e) {
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(path.equals("/UpdateAccount.do")) {
			long accountId=Long.parseLong(request.getParameter("account_id"));
			String accountHolderName= request.getParameter("customer_name");
			String accountType= request.getParameter("account_type");
			boolean result= bankService.updateAccountDetails(accountId, accountHolderName, accountType);
			if(result) {
				response.sendRedirect("getAllBankAccounts.do");
				out.println("updated successfull..");
				out.println("<h2><a href='index.html'>|home|</h2>");
			}
			else {
				out.println("failed..");
				out.println("<h2><a href='index.html'>|home|</h2>");
			}
		}
	}
}
