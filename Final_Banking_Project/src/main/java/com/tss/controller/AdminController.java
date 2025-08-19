package com.tss.controller;

import java.io.IOException;
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
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Admin admin = adminDAO.validateAdmin(username, password);

		if (admin != null) {
			HttpSession session = request.getSession();
			session.setAttribute("admin", admin);
			response.sendRedirect("adminDashboard");
		} else {
			request.setAttribute("errorMessage", "Invalid admin credentials.");
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	}

	private void showDashboard(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<CustomerAccountView> allCustomers = customerDAO.getAllCustomerAccountDetails();
		request.setAttribute("customers", allCustomers);
		request.getRequestDispatcher("/WEB-INF/jsp/adminDashboard.jsp").forward(request, response);
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

		Account account = accountDAO.getAccountByCustomerId(customerId);

		if (account != null) {
			List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(account.getAccountId());

			request.setAttribute("transactions", transactions);
			request.setAttribute("customerName", customerDAO.getCustomerById(customerId).getFullName());
			request.setAttribute("accountNumber", account.getAccountNumber());
		} else {
			
			System.out.println("AdminController: No account found for customer ID: " + customerId);
			request.setAttribute("errorMessage", "No account found for this customer.");
		}

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
