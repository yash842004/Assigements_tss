package com.tss.controller;

import java.io.IOException;
import java.util.ArrayList;
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
		case "/login":
		    RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
		    dispatcher.forward(request, response);
		    break;
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
		try {
			String fullName = request.getParameter("fullName");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String address = request.getParameter("address");
			String phone = request.getParameter("phone");

			if (fullName == null || fullName.trim().isEmpty() || 
				email == null || email.trim().isEmpty() || 
				password == null || password.trim().isEmpty() || 
				address == null || address.trim().isEmpty() || 
				phone == null || phone.trim().isEmpty()) {
				request.setAttribute("errorMessage", "All fields are required.");
				RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
				dispatcher.forward(request, response);
				return;
			}

			if (!email.contains("@") || !email.contains(".")) {
				request.setAttribute("errorMessage", "Please enter a valid email address.");
				RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
				dispatcher.forward(request, response);
				return;
			}

			Customer newCustomer = new Customer();
			newCustomer.setFullName(fullName.trim());
			newCustomer.setEmail(email.trim().toLowerCase());
			newCustomer.setPassword(password); 
			newCustomer.setAddress(address.trim());
			newCustomer.setPhone(phone.trim());

			System.out.println("CustomerController: Registering new customer: " + email);
			customerDAO.registerCustomer(newCustomer);

			request.setAttribute("successMessage",
					"Registration successful! Your account is pending approval from an admin.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			System.err.println("CustomerController: Error during registration: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred during registration. Please try again.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void loginCustomer(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
				request.setAttribute("errorMessage", "Please enter both email and password.");
				RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
				dispatcher.forward(request, response);
				return;
			}
			
			System.out.println("CustomerController: Login attempt for email: " + email);
			
			Customer customer = customerDAO.getCustomerByEmailAndPassword(email.trim(), password);

			if (customer != null) {
				HttpSession session = request.getSession();
				session.setAttribute("customer", customer);
				session.setMaxInactiveInterval(1800); 
				System.out.println("CustomerController: Login successful for customer: " + customer.getFullName());
				response.sendRedirect("dashboard");
			} else {
				System.out.println("CustomerController: Login failed for email: " + email);
				request.setAttribute("errorMessage", "Invalid email or password, or your account is not yet approved.");
				RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
				dispatcher.forward(request, response);
			}
		} catch (Exception e) {
			System.err.println("CustomerController: Error during login: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred during login. Please try again.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void showDashboard(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute("customer") == null) {
				response.sendRedirect("index.jsp"); 
				return;
			}

			Customer customer = (Customer) session.getAttribute("customer");
			System.out.println("CustomerController: Loading dashboard for customer: " + customer.getFullName());
			
			List<Account> allAccounts = customerService.getAllCustomerAccounts(customer.getCustomerId());
			
			if (allAccounts == null) {
				allAccounts = new ArrayList<>();
			}
			
			Account account = allAccounts.isEmpty() ? null : allAccounts.get(0);
			
			request.setAttribute("account", account);
			request.setAttribute("allAccounts", allAccounts);
			System.out.println("CustomerController: Found " + allAccounts.size() + " accounts for customer");
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("customerDashboard.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			System.err.println("CustomerController: Error loading dashboard: " + e.getMessage());
			e.printStackTrace();
			response.sendRedirect("index.jsp");
		}
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
		try {
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute("customer") == null) {
				response.sendRedirect("index.jsp");
				return;
			}
			
			Customer customer = (Customer) session.getAttribute("customer");
			String accountType = request.getParameter("type");
			
			if (accountType == null || accountType.trim().isEmpty()) {
				request.setAttribute("errorMessage", "Please select an account type.");
				showOpenAccountForm(request, response);
				return;
			}
			
			System.out.println("CustomerController: Creating account of type: " + accountType + " for customer: " + customer.getFullName());
			
			boolean created = customerService.createAccountForCustomer(customer.getCustomerId(), accountType.trim());
			if (!created) {
				request.setAttribute("errorMessage",
						"Account not created. It may already exist for this type or the type is invalid.");
				showOpenAccountForm(request, response);
				return;
			}
			
			System.out.println("CustomerController: Account created successfully");
			request.setAttribute("successMessage", "Account created successfully.");
			showDashboard(request, response);
		} catch (Exception e) {
			System.err.println("CustomerController: Error creating account: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred while creating the account. Please try again.");
			showOpenAccountForm(request, response);
		}
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
		try {
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute("customer") == null) {
				response.sendRedirect("index.jsp");
				return;
			}

			Customer customer = (Customer) session.getAttribute("customer");
			System.out.println("CustomerController: Loading manage accounts for customer: " + customer.getFullName());
			
			List<Account> accounts = customerService.getAllCustomerAccounts(customer.getCustomerId());
			
			if (accounts == null) {
				accounts = new ArrayList<>();
			}
			
			System.out.println("CustomerController: Found " + accounts.size() + " accounts for customer");
			request.setAttribute("accounts", accounts);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("manageAccounts.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			System.err.println("CustomerController: Error loading manage accounts: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred while loading accounts.");
			response.sendRedirect("dashboard");
		}
	}

	private void updateAccountType(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute("customer") == null) {
				response.sendRedirect("index.jsp");
				return;
			}

			Customer customer = (Customer) session.getAttribute("customer");
			String accountIdStr = request.getParameter("accountId");
			String newAccountType = request.getParameter("newAccountType");
			
			if (accountIdStr == null || accountIdStr.trim().isEmpty() || 
				newAccountType == null || newAccountType.trim().isEmpty()) {
				request.setAttribute("errorMessage", "Account ID and new account type are required.");
				showManageAccounts(request, response);
				return;
			}
			
			int accountId;
			try {
				accountId = Integer.parseInt(accountIdStr);
			} catch (NumberFormatException e) {
				request.setAttribute("errorMessage", "Invalid account ID format.");
				showManageAccounts(request, response);
				return;
			}
			
			System.out.println("CustomerController: Customer " + customer.getFullName() + " attempting to update account ID: " + accountId + " to type: " + newAccountType);
			
			// Verify the account belongs to the customer
			Account account = accountDAO.getAccountById(accountId);
			if (account == null) {
				request.setAttribute("errorMessage", "Account not found.");
				showManageAccounts(request, response);
				return;
			}
			
			if (account.getCustomerId() != customer.getCustomerId()) {
				request.setAttribute("errorMessage", "You can only update your own accounts.");
				showManageAccounts(request, response);
				return;
			}

			boolean updated = customerService.updateAccountType(accountId, newAccountType);
			if (updated) {
				System.out.println("CustomerController: Account type updated successfully");
				request.setAttribute("successMessage", "Account type updated successfully.");
			} else {
				System.out.println("CustomerController: Failed to update account type");
				request.setAttribute("errorMessage", "Failed to update account type.");
			}

			showManageAccounts(request, response);
		} catch (Exception e) {
			System.err.println("CustomerController: Error during account type update: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred while updating the account type. Please try again.");
			showManageAccounts(request, response);
		}
	}

	private void deleteAccount(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute("customer") == null) {
				response.sendRedirect("index.jsp");
				return;
			}

			Customer customer = (Customer) session.getAttribute("customer");
			String accountIdStr = request.getParameter("accountId");
			
			if (accountIdStr == null || accountIdStr.trim().isEmpty()) {
				request.setAttribute("errorMessage", "Account ID is required.");
				showManageAccounts(request, response);
				return;
			}
			
			int accountId;
			try {
				accountId = Integer.parseInt(accountIdStr);
			} catch (NumberFormatException e) {
				request.setAttribute("errorMessage", "Invalid account ID format.");
				showManageAccounts(request, response);
				return;
			}
			
			System.out.println("CustomerController: Customer " + customer.getFullName() + " attempting to delete account ID: " + accountId);
			
			// Verify the account belongs to the customer
			Account account = accountDAO.getAccountById(accountId);
			if (account == null) {
				request.setAttribute("errorMessage", "Account not found.");
				showManageAccounts(request, response);
				return;
			}
			
			if (account.getCustomerId() != customer.getCustomerId()) {
				request.setAttribute("errorMessage", "You can only delete your own accounts.");
				showManageAccounts(request, response);
				return;
			}
			
			if (account.getBalance().compareTo(java.math.BigDecimal.ZERO) > 0) {
				request.setAttribute("errorMessage", "Cannot delete account with remaining balance. Please withdraw all funds first.");
				showManageAccounts(request, response);
				return;
			}
			
			boolean hasTransactions = accountDAO.hasTransactions(accountId);
			if (hasTransactions) {
				System.out.println("CustomerController: Account has transactions, will delete them along with the account");
			}

			boolean deleted = customerService.deleteAccount(accountId);
			if (deleted) {
				System.out.println("CustomerController: Account deleted successfully");
				if (hasTransactions) {
					request.setAttribute("successMessage", "Account and all associated transactions deleted successfully.");
				} else {
					request.setAttribute("successMessage", "Account deleted successfully.");
				}
			} else {
				System.out.println("CustomerController: Failed to delete account");
				request.setAttribute("errorMessage", "Failed to delete account. Please try again.");
			}

			showManageAccounts(request, response);
		} catch (Exception e) {
			System.err.println("CustomerController: Error during account deletion: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred while deleting the account. Please try again.");
			showManageAccounts(request, response);
		}
	}
}