package com.tss.controller;

import java.io.IOException;
import java.util.List;

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
import com.tss.service.CustomerService;


@WebServlet(urlPatterns = { "/register", "/login", "/dashboard", "/logout", "/openAccount", "/manageAccounts", "/updateAccountType", "/deleteAccount" })
public class CustomerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final CustomerDAO customerDAO;
	private final AccountDAO accountDAO;
	private final CustomerService customerService;

	public CustomerController() {
		super();
		this.customerDAO = new CustomerDAO();
		this.accountDAO = new AccountDAO();
		this.customerService = new CustomerService();
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		switch (action) {
		case "/dashboard":
			showDashboard(request, response);
			break;
		case "/openAccount":
			showOpenAccountForm(request, response);
			break;
		case "/manageAccounts":
			showManageAccounts(request, response);
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
		case "/openAccount":
			createAccount(request, response);
			break;
		case "/updateAccountType":
			updateAccountType(request, response);
			break;
		case "/deleteAccount":
			deleteAccount(request, response);
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
		List<Account> allAccounts = customerService.getAllCustomerAccounts(customer.getCustomerId());
		
		// For backward compatibility, keep the first account as the main account
		Account account = allAccounts.isEmpty() ? null : allAccounts.get(0);
		
		request.setAttribute("account", account);
		request.setAttribute("allAccounts", allAccounts);
		RequestDispatcher dispatcher = request.getRequestDispatcher("customerDashboard.jsp");
		dispatcher.forward(request, response);
	}

	private void showOpenAccountForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("customer") == null) {
			response.sendRedirect("index.jsp");
			return;
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("openAccount.jsp");
		dispatcher.forward(request, response);
	}

	private void createAccount(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("customer") == null) {
			response.sendRedirect("index.jsp");
			return;
		}
		Customer customer = (Customer) session.getAttribute("customer");
		String accountType = request.getParameter("type");
		boolean created = customerService.createAccountForCustomer(customer.getCustomerId(), accountType);
		if (!created) {
			request.setAttribute("errorMessage",
					"Account not created. It may already exist for this type or the type is invalid.");
			showOpenAccountForm(request, response);
			return;
		}
		request.setAttribute("successMessage", "Account created successfully.");
		showDashboard(request, response);
	}

	private void logoutCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		response.sendRedirect("index.jsp");
	}

	private void showManageAccounts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("customer") == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		Customer customer = (Customer) session.getAttribute("customer");
		List<Account> accounts = customerService.getAllCustomerAccounts(customer.getCustomerId());

		request.setAttribute("accounts", accounts);
		RequestDispatcher dispatcher = request.getRequestDispatcher("manageAccounts.jsp");
		dispatcher.forward(request, response);
	}

	private void updateAccountType(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("customer") == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		int accountId = Integer.parseInt(request.getParameter("accountId"));
		String newAccountType = request.getParameter("newAccountType");

		boolean updated = customerService.updateAccountType(accountId, newAccountType);
		if (updated) {
			request.setAttribute("successMessage", "Account type updated successfully.");
		} else {
			request.setAttribute("errorMessage", "Failed to update account type.");
		}

		showManageAccounts(request, response);
	}

	private void deleteAccount(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("customer") == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		int accountId = Integer.parseInt(request.getParameter("accountId"));

		boolean deleted = customerService.deleteAccount(accountId);
		if (deleted) {
			request.setAttribute("successMessage", "Account deleted successfully.");
		} else {
			request.setAttribute("errorMessage", "Failed to delete account.");
		}

		showManageAccounts(request, response);
	}
}
