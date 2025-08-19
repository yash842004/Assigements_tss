package com.tss.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tss.model.Customer;
import com.tss.service.AccountService;
import com.tss.service.CustomerService;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CustomerService customerService = new CustomerService();

	public RegisterServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Customer customer = new Customer();
		customer.setUsername(req.getParameter("username"));
		customer.setPassword(req.getParameter("password"));
		customer.setFullName(req.getParameter("fullName"));
		customer.setEmail(req.getParameter("email"));
		customer.setRole("customer");

		String accountType = req.getParameter("accountType");

		customerService.register(customer); 
		AccountService accountService = new AccountService();
		accountService.createAccountForCustomer(customer.getId(), accountType);

		resp.sendRedirect("/login");

	}

}
