package com.capgemini.news.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/getNews"},loadOnStartup=1)
public class NewsAppController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public NewsAppController() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String path = request.getParameter("paper");
		System.out.println(path);
		if (path.equals("TOI")) {
			response.sendRedirect("https://www.TimesOfIndia.com/");

		} else if (path.equals("ET")) {
			response.sendRedirect("https://www.EconomicsTimes.com/");

		} else if (path.equals("MT")) {
			response.sendRedirect("https://www.MaharashtraTimes.com/");

		} else if (path.equals("DNA")) {
			response.sendRedirect("https://www.DNA.com/");

		}


	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
	}

}
