package com.capgemini.credit.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(loadOnStartup = 1, urlPatterns= "*.do")
public class CreditCardController extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public CreditCardController() 
    {
        super();
        
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String path = request.getServletPath();
		if(path.equals("/CreditCard.do"))
		{
			response.setContentType("text/html");
			String customerName = request.getParameter("customer_name");
			long cardNumber = Long.parseLong(request.getParameter("card_number"));
			int cvv = Integer.parseInt(request.getParameter("cvv"));
			double creditLimit = Double.parseDouble(request.getParameter("credit_limit"));
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("CreditCardDetails.jsp");
			
			request.setAttribute("customerName", customerName);
			request.setAttribute("CardNumber", cardNumber);
			request.setAttribute("CVV", cvv);
			request.setAttribute("CreditLimit", creditLimit);
			dispatcher.forward(request, response);
		}
		
	}

}
