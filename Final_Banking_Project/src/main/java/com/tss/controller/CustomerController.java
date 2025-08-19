package com.tss.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.dao.AccountDAO;
import com.tss.dao.CustomerDAO;
import com.tss.model.Account;
import com.tss.model.Customer;


@WebServlet(urlPatterns = { "/register", "/login", "/dashboard", "/logout" })
public class CustomerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final CustomerDAO customerDAO;
	private final AccountDAO accountDAO;

	public CustomerController() {
		super();
		this.customerDAO = new CustomerDAO();
		this.accountDAO = new AccountDAO();
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		switch (action) {
		case "/dashboard":
			showDashboard(request, response);
			break;
		case "/logout":
			logoutCustomer(request, response);
			break;
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		switch (action) {
		case "/register":
			registerCustomer(request, response);
			break;
		case "/login":
			loginCustomer(request, response);
			break;
		}
	}

	private void registerCustomer(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String fullName = request.getParameter("fullName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");

		Customer newCustomer = new Customer();
		newCustomer.setFullName(fullName);
		newCustomer.setEmail(email);
		newCustomer.setPassword(password); // Remember to hash passwords in a real application
		newCustomer.setAddress(address);
		newCustomer.setPhone(phone);

		customerDAO.registerCustomer(newCustomer);

		request.setAttribute("successMessage",
				"Registration successful! Your account is pending approval from an admin.");
		RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
		dispatcher.forward(request, response);
	}

	private void loginCustomer(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		Customer customer = customerDAO.getCustomerByEmailAndPassword(email, password);

		if (customer != null) {
			HttpSession session = request.getSession();
			session.setAttribute("customer", customer);
			response.sendRedirect("dashboard");
		} else {
			request.setAttribute("errorMessage", "Invalid email or password, or your account is not yet approved.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void showDashboard(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("customer") == null) {
			response.sendRedirect("index.jsp"); // Not logged in, redirect to login
			return;
		}

		Customer customer = (Customer) session.getAttribute("customer");
		Account account = accountDAO.getAccountByCustomerId(customer.getCustomerId());

		request.setAttribute("account", account);
		RequestDispatcher dispatcher = request.getRequestDispatcher("customerDashboard.jsp");
		dispatcher.forward(request, response);
	}

	private void logoutCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		response.sendRedirect("index.jsp");
	}

}
