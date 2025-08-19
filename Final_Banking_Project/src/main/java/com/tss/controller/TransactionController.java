package com.tss.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.dao.AccountDAO;
import com.tss.dao.TransactionDAO;
import com.tss.model.Account;
import com.tss.model.Customer;
import com.tss.model.Transaction;
import com.tss.service.AccountService;

@WebServlet(urlPatterns = { "/deposit", "/withdraw", "/transfer", "/passbook" })
public class TransactionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final AccountService accountService;
	private final AccountDAO accountDAO;
	private final TransactionDAO transactionDAO;

	public TransactionController() {
		super();
		this.accountService = new AccountService();
		this.accountDAO = new AccountDAO();
		this.transactionDAO = new TransactionDAO();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("customer") == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		switch (action) {
		case "/deposit":
			handleDeposit(request, response);
			break;
		case "/withdraw":
			handleWithdraw(request, response);
			break;
		case "/transfer":
			handleTransfer(request, response);
			break;
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("customer") == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		if ("/passbook".equals(action)) {
			showPassbook(request, response);
		}
	}

	private void handleDeposit(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute("customer");
		BigDecimal amount = new BigDecimal(request.getParameter("amount"));

		Account account = accountDAO.getAccountByCustomerId(customer.getCustomerId());
		boolean success = accountService.deposit(account.getAccountId(), amount);

		if (success) {
			response.sendRedirect("dashboard");
		} else {
			request.setAttribute("errorMessage", "Deposit failed. Please enter a valid amount.");
			request.getRequestDispatcher("/WEB-INF/jsp/deposit.jsp").forward(request, response);
		}
	}

	private void handleWithdraw(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute("customer");
		BigDecimal amount = new BigDecimal(request.getParameter("amount"));

		Account account = accountDAO.getAccountByCustomerId(customer.getCustomerId());
		boolean success = accountService.withdraw(account.getAccountId(), amount);

		if (success) {
			response.sendRedirect("dashboard");
		} else {
			request.setAttribute("errorMessage", "Withdrawal failed. Check for sufficient funds.");
			request.getRequestDispatcher("/WEB-INF/jsp/withdraw.jsp").forward(request, response);
		}
	}

	private void handleTransfer(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute("customer");
		String toAccountNumber = request.getParameter("toAccountNumber");
		BigDecimal amount = new BigDecimal(request.getParameter("amount"));

		Account fromAccount = accountDAO.getAccountByCustomerId(customer.getCustomerId());
		String message = accountService.transferMoney(fromAccount.getAccountId(), toAccountNumber, amount);

		if ("Transfer successful!".equals(message)) {
			response.sendRedirect("dashboard");
		} else {
			request.setAttribute("errorMessage", message);
			request.getRequestDispatcher("/WEB-INF/jsp/transfer.jsp").forward(request, response);
		}
	}

	private void showPassbook(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute("customer");

		// Use the customer's ID from the session to find their account
		Account account = accountDAO.getAccountByCustomerId(customer.getCustomerId());

		// **FIX**: Check if the account was actually found
		if (account != null) {
			// If found, use the account's ID to get its transactions
			List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(account.getAccountId());
			request.setAttribute("transactions", transactions);

			// This line is for debugging - check your server console (e.g., in
			// Eclipse/IntelliJ)
			System.out.println(
					"Found " + transactions.size() + " transactions for account ID: " + account.getAccountId());

		} else {
			// This handles the case where no account is linked to the customer
			// This line will print to your server console if the account is not found
			System.out.println("CRITICAL: No account found for customer ID: " + customer.getCustomerId());
			request.setAttribute("transactions", Collections.emptyList()); // Send an empty list to avoid errors on the
																			// JSP page
		}

		request.getRequestDispatcher("/passbook.jsp").forward(request, response);
	}

}
