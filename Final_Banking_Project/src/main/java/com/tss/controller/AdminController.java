package com.tss.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.tss.dao.AccountDAO;
import com.tss.dao.AdminDAO;
import com.tss.dao.CustomerDAO;
import com.tss.dao.TransactionDAO;
import com.tss.model.Account;
import com.tss.model.Admin;
import com.tss.model.CustomerAccountView;
import com.tss.model.Transaction;
import com.tss.service.CustomerService;

@WebServlet(urlPatterns = { "/adminLogin", "/adminDashboard", "/approveCustomer", "/rejectCustomer", "/adminLogout",
		"/viewTransactions", "/activateCustomer", "/deactivateCustomer", "/getAnalyticsData" })
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
		case "/getAnalyticsData":
			getAnalyticsData(request, response);
			break;

		case "/adminLogout":
			logoutAdmin(request, response);
			break;
		}
	}

	private void loginAdmin(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		Admin admin = adminDAO.validateAdmin(username, password);

		if (admin != null) {
			HttpSession session = request.getSession();
			session.setAttribute("admin", admin);
			response.sendRedirect("adminDashboard");
		} else {
			request.setAttribute("error", "Invalid username or password");
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	}

	private void getAnalyticsData(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		Map<String, Object> transactionData = transactionDAO.getTransactionAnalytics();
		Map<String, Long> accountTypeData = accountDAO.getAccountTypeDistribution();

		AnalyticsData data = new AnalyticsData(transactionData, accountTypeData);

		String json = new Gson().toJson(data);
		response.getWriter().write(json);
	}

	private static class AnalyticsData {
		private final Map<String, Object> transactionData;
		private final Map<String, Long> accountTypeData;

		public AnalyticsData(Map<String, Object> transactionData, Map<String, Long> accountTypeData) {
			this.transactionData = transactionData;
			this.accountTypeData = accountTypeData;
		}
	}

	private void showDashboard(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<CustomerAccountView> pendingCustomers = customerService.getPendingCustomers();
		List<CustomerAccountView> approvedCustomers = customerService.getApprovedCustomers();
		List<CustomerAccountView> rejectedCustomers = customerService.getRejectedCustomers();

		request.setAttribute("pendingCustomers", pendingCustomers);
		request.setAttribute("approvedCustomers", approvedCustomers);
		request.setAttribute("rejectedCustomers", rejectedCustomers);

		request.getRequestDispatcher("/adminDashboard.jsp").forward(request, response);
	}

	private void approveCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int customerId = Integer.parseInt(request.getParameter("id"));
		customerService.approveCustomer(customerId);
		response.sendRedirect("adminDashboard");
	}

	private void rejectCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int customerId = Integer.parseInt(request.getParameter("id"));
		customerService.rejectCustomer(customerId);
		response.sendRedirect("adminDashboard");
	}

	private void activateCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int customerId = Integer.parseInt(request.getParameter("id"));
		customerDAO.updateCustomerStatus(customerId, "APPROVED");
		response.sendRedirect("adminDashboard");
	}

	private void deactivateCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int customerId = Integer.parseInt(request.getParameter("id"));
		customerDAO.updateCustomerStatus(customerId, "DEACTIVATED");
		response.sendRedirect("adminDashboard");
	}

	private void viewCustomerTransactions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int customerId = Integer.parseInt(request.getParameter("id"));

		List<Account> accounts = accountDAO.getAccountsByCustomerIdAll(customerId);

		if (accounts.isEmpty()) {
			request.setAttribute("errorMessage", "No accounts found for this customer.");
			request.setAttribute("transactions", Collections.emptyList());
			request.setAttribute("customerName", customerDAO.getCustomerById(customerId).getFullName());
			request.getRequestDispatcher("adminViewTransactions.jsp").forward(request, response);
			return;
		}

		String accountIdParam = request.getParameter("accountId");
		int selectedAccountId;

		if (accountIdParam != null && !accountIdParam.trim().isEmpty()) {
			selectedAccountId = Integer.parseInt(accountIdParam);
		} else {
			selectedAccountId = accounts.get(0).getAccountId();
		}

		Account selectedAccount = accountDAO.getAccountById(selectedAccountId);
		List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(selectedAccountId);

		request.setAttribute("transactions", transactions);
		request.setAttribute("accounts", accounts);
		request.setAttribute("selectedAccountId", selectedAccountId);
		request.setAttribute("customerName", customerDAO.getCustomerById(customerId).getFullName());
		request.setAttribute("accountNumber", selectedAccount.getAccountNumber());

		request.getRequestDispatcher("adminViewTransactions.jsp").forward(request, response);
	}

	private void logoutAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		response.sendRedirect("index.jsp");
	}

}
