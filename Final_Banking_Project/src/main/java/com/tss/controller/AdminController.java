package com.tss.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.dao.AccountDAO;
import com.tss.dao.AdminDAO;
import com.tss.dao.CustomerDAO;
import com.tss.dao.TransactionDAO;
import com.tss.model.Account;
import com.tss.model.Admin;
import com.tss.model.Customer;
import com.tss.model.CustomerAccountView;
import com.tss.model.Transaction;
import com.tss.service.CustomerService;

@WebServlet(urlPatterns = { "/adminLogin", "/adminDashboard", "/approveCustomer", "/rejectCustomer", "/adminLogout",
		"/viewTransactions", "/activateCustomer", "/deactivateCustomer" })
public class AdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final AdminDAO adminDAO;
	private final CustomerDAO customerDAO;
	private final AccountDAO accountDAO;
	private final TransactionDAO transactionDAO;
	private final CustomerService customerService;

	public AdminController() {
		super();
		this.adminDAO = new AdminDAO();
		this.customerDAO = new CustomerDAO();
		this.accountDAO = new AccountDAO();
		this.transactionDAO = new TransactionDAO();
		this.customerService = new CustomerService();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		if ("/adminLogin".equals(action)) {
			loginAdmin(request, response);
		} else {
			doGet(request, response);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		HttpSession session = request.getSession(false);

		if (!"/adminLogin".equals(action) && (session == null || session.getAttribute("admin") == null)) {
			response.sendRedirect("index.jsp");
			return;
		}

		switch (action) {
		case "/adminDashboard":
			showDashboard(request, response);
			break;
		case "/approveCustomer":
			approveCustomer(request, response);
			break;
		case "/rejectCustomer":
			rejectCustomer(request, response);
			break;
		case "/activateCustomer":
			activateCustomer(request, response);
			break;
		case "/deactivateCustomer":
			deactivateCustomer(request, response);
			break;
		case "/viewTransactions":
			viewCustomerTransactions(request, response);
			break;
		case "/adminLogout":
			logoutAdmin(request, response);
			break;
		}
	}

	private void loginAdmin(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
				request.setAttribute("errorMessage", "Please enter both username and password.");
				request.getRequestDispatcher("index.jsp").forward(request, response);
				return;
			}

			Admin admin = adminDAO.validateAdmin(username.trim(), password);

			if (admin != null) {
				HttpSession session = request.getSession();
				session.setAttribute("admin", admin);
				session.setMaxInactiveInterval(1800); // 30 minutes
				response.sendRedirect("adminDashboard");
			} else {
				request.setAttribute("errorMessage", "Invalid admin credentials. Please try again.");
				request.getRequestDispatcher("index.jsp").forward(request, response);
			}
		} catch (Exception e) {
			System.err.println("AdminController: Error during admin login: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred during login. Please try again.");
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	}

	private void showDashboard(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			List<CustomerAccountView> allCustomers = customerDAO.getAllCustomerAccountDetails();

			if (allCustomers == null) {
				allCustomers = Collections.emptyList();
			}

			request.setAttribute("customers", allCustomers);
			request.getRequestDispatcher("/adminDashboard.jsp").forward(request, response);
		} catch (Exception e) {
			System.err.println("AdminController: Error loading dashboard: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred while loading the dashboard.");
			request.getRequestDispatcher("/adminDashboard.jsp").forward(request, response);
		}
	}

	private void approveCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String customerIdStr = request.getParameter("id");

			if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
				response.sendRedirect("adminDashboard");
				return;
			}

			int customerId;
			try {
				customerId = Integer.parseInt(customerIdStr);
			} catch (NumberFormatException e) {
				System.err.println("AdminController: Invalid customer ID format: " + customerIdStr);
				response.sendRedirect("adminDashboard");
				return;
			}

			boolean success = customerService.approveCustomer(customerId);

			if (success) {
				System.out.println("AdminController: Customer approved successfully");
			} else {
				System.err.println("AdminController: Failed to approve customer");
			}

			response.sendRedirect("adminDashboard");
		} catch (Exception e) {
			System.err.println("AdminController: Error approving customer: " + e.getMessage());
			e.printStackTrace();
			response.sendRedirect("adminDashboard");
		}
	}

	private void rejectCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String customerIdStr = request.getParameter("id");

			if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
				response.sendRedirect("adminDashboard");
				return;
			}

			int customerId;
			try {
				customerId = Integer.parseInt(customerIdStr);
			} catch (NumberFormatException e) {
				System.err.println("AdminController: Invalid customer ID format: " + customerIdStr);
				response.sendRedirect("adminDashboard");
				return;
			}

			boolean success = customerService.rejectCustomer(customerId);

			if (success) {
				System.out.println("AdminController: Customer rejected successfully");
			} else {
				System.err.println("AdminController: Failed to reject customer");
			}

			response.sendRedirect("adminDashboard");
		} catch (Exception e) {
			System.err.println("AdminController: Error rejecting customer: " + e.getMessage());
			e.printStackTrace();
			response.sendRedirect("adminDashboard");
		}
	}

	private void activateCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String customerIdStr = request.getParameter("id");

			if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
				response.sendRedirect("adminDashboard");
				return;
			}

			int customerId;
			try {
				customerId = Integer.parseInt(customerIdStr);
			} catch (NumberFormatException e) {
				System.err.println("AdminController: Invalid customer ID format: " + customerIdStr);
				response.sendRedirect("adminDashboard");
				return;
			}

			System.out.println("AdminController: Activating customer ID: " + customerId);

			boolean success = customerDAO.updateCustomerStatus(customerId, "APPROVED");

			if (success) {
				System.out.println("AdminController: Customer activated successfully");
			} else {
				System.err.println("AdminController: Failed to activate customer");
			}

			response.sendRedirect("adminDashboard");
		} catch (Exception e) {
			System.err.println("AdminController: Error activating customer: " + e.getMessage());
			e.printStackTrace();
			response.sendRedirect("adminDashboard");
		}
	}

	private void deactivateCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String customerIdStr = request.getParameter("id");

			if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
				response.sendRedirect("adminDashboard");
				return;
			}

			int customerId;
			try {
				customerId = Integer.parseInt(customerIdStr);
			} catch (NumberFormatException e) {
				System.err.println("AdminController: Invalid customer ID format: " + customerIdStr);
				response.sendRedirect("adminDashboard");
				return;
			}

			boolean success = customerDAO.updateCustomerStatus(customerId, "DEACTIVATED");

			if (success) {
				System.out.println("AdminController: Customer deactivated successfully");
			} else {
				System.err.println("AdminController: Failed to deactivate customer");
			}

			response.sendRedirect("adminDashboard");
		} catch (Exception e) {
			System.err.println("AdminController: Error deactivating customer: " + e.getMessage());
			e.printStackTrace();
			response.sendRedirect("adminDashboard");
		}
	}

	private void viewCustomerTransactions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String customerIdStr = request.getParameter("id");

			if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
				request.setAttribute("errorMessage", "Customer ID is required.");
				request.getRequestDispatcher("adminViewTransactions.jsp").forward(request, response);
				return;
			}

			int customerId;
			try {
				customerId = Integer.parseInt(customerIdStr);
			} catch (NumberFormatException e) {
				System.err.println("AdminController: Invalid customer ID format: " + customerIdStr);
				request.setAttribute("errorMessage", "Invalid customer ID format.");
				request.getRequestDispatcher("adminViewTransactions.jsp").forward(request, response);
				return;
			}

			System.out.println("AdminController: Viewing transactions for customer ID: " + customerId);

			Customer customer = customerDAO.getCustomerById(customerId);
			if (customer == null) {
				request.setAttribute("errorMessage", "Customer not found.");
				request.getRequestDispatcher("adminViewTransactions.jsp").forward(request, response);
				return;
			}

			request.setAttribute("customerName", customer.getFullName());
			System.out.println("AdminController: Customer name: " + customer.getFullName());

			List<Account> accounts = accountDAO.getAccountsByCustomerIdAll(customerId);

			if (accounts == null) {
				accounts = Collections.emptyList();
			}

			System.out.println("AdminController: Found " + accounts.size() + " accounts for customer");

			if (accounts.isEmpty()) {
				request.setAttribute("errorMessage", "No accounts found for this customer.");
				request.setAttribute("transactions", Collections.emptyList());
				request.setAttribute("accounts", accounts);
				request.getRequestDispatcher("adminViewTransactions.jsp").forward(request, response);
				return;
			}

			request.setAttribute("accounts", accounts);

			int selectedAccountId = 0;
			String accountIdParam = request.getParameter("accountId");
			if (accountIdParam != null && !accountIdParam.isEmpty()) {
				try {
					selectedAccountId = Integer.parseInt(accountIdParam);
					boolean accountBelongsToCustomer = false;
					for (Account account : accounts) {
						if (account.getAccountId() == selectedAccountId) {
							accountBelongsToCustomer = true;
							break;
						}
					}
					if (!accountBelongsToCustomer) {
						selectedAccountId = accounts.get(0).getAccountId();
					}
				} catch (NumberFormatException e) {
					selectedAccountId = accounts.get(0).getAccountId();
				}
			} else if (!accounts.isEmpty()) {
				selectedAccountId = accounts.get(0).getAccountId();
			}

			request.setAttribute("selectedAccountId", selectedAccountId);

			Account selectedAccount = null;
			for (Account account : accounts) {
				if (account.getAccountId() == selectedAccountId) {
					selectedAccount = account;
					break;
				}
			}

			if (selectedAccount != null) {
				request.setAttribute("accountNumber", selectedAccount.getAccountNumber());

				List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(selectedAccountId);

				if (transactions == null) {
					transactions = Collections.emptyList();
				}

				request.setAttribute("transactions", transactions);
			} else {
				request.setAttribute("transactions", Collections.emptyList());
			}

			request.getRequestDispatcher("adminViewTransactions.jsp").forward(request, response);
		} catch (Exception e) {
			System.err.println("AdminController: Error viewing transactions: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred while loading transactions.");
			request.getRequestDispatcher("adminViewTransactions.jsp").forward(request, response);
		}
	}

	private void logoutAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			HttpSession session = request.getSession(false);
			if (session != null) {
				System.out.println("AdminController: Admin logout successful");
				session.invalidate();
			}
			response.sendRedirect("index.jsp");
		} catch (Exception e) {
			System.err.println("AdminController: Error during admin logout: " + e.getMessage());
			e.printStackTrace();
			response.sendRedirect("index.jsp");
		}
	}
}