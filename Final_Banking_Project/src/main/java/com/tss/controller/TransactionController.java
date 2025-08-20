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

		switch (action) {
		case "/passbook":
			showPassbook(request, response);
			break;
		case "/deposit":
			showDepositForm(request, response);
			break;
		case "/withdraw":
			showWithdrawForm(request, response);
			break;
		case "/transfer":
			showTransferForm(request, response);
			break;
		}
	}

	private void handleDeposit(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute("customer");
		BigDecimal amount = new BigDecimal(request.getParameter("amount"));

		int accountId = Integer.parseInt(request.getParameter("accountId"));
		Account account = accountDAO.getAccountById(accountId);
		if (account == null || account.getCustomerId() != customer.getCustomerId()) {
			request.setAttribute("errorMessage", "Invalid account selection.");
			showDepositForm(request, response);
			return;
		}
		boolean success = accountService.deposit(accountId, amount);

		if (success) {
			response.sendRedirect("dashboard");
		} else {
			request.setAttribute("errorMessage", "Deposit failed. Please enter a valid amount.");
			showDepositForm(request, response);
		}
	}

	private void handleWithdraw(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute("customer");
		BigDecimal amount = new BigDecimal(request.getParameter("amount"));

		int accountId = Integer.parseInt(request.getParameter("accountId"));
		Account account = accountDAO.getAccountById(accountId);
		if (account == null || account.getCustomerId() != customer.getCustomerId()) {
			request.setAttribute("errorMessage", "Invalid account selection.");
			showWithdrawForm(request, response);
			return;
		}
		boolean success = accountService.withdraw(accountId, amount);

		if (success) {
			response.sendRedirect("dashboard");
		} else {
			request.setAttribute("errorMessage", "Withdrawal failed. Check for sufficient funds.");
			showWithdrawForm(request, response);
		}
	}

	private void handleTransfer(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute("customer");
		String toAccountNumber = request.getParameter("toAccountNumber");
		BigDecimal amount = new BigDecimal(request.getParameter("amount"));

		int fromAccountId = Integer.parseInt(request.getParameter("fromAccountId"));
		Account fromAccount = accountDAO.getAccountById(fromAccountId);
		if (fromAccount == null || fromAccount.getCustomerId() != customer.getCustomerId()) {
			request.setAttribute("errorMessage", "Invalid source account selection.");
			showTransferForm(request, response);
			return;
		}
		String message = accountService.transferMoney(fromAccountId, toAccountNumber, amount);

		if ("Transfer successful!".equals(message)) {
			response.sendRedirect("dashboard");
		} else {
			request.setAttribute("errorMessage", message);
			showTransferForm(request, response);
		}
	}

	private void showPassbook(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute("customer");

		// Get all accounts for the customer
		List<Account> accounts = accountDAO.getAccountsByCustomerIdAll(customer.getCustomerId());
		
		if (accounts.isEmpty()) {
			request.setAttribute("transactions", Collections.emptyList());
			request.setAttribute("accounts", accounts);
			request.getRequestDispatcher("/passbook.jsp").forward(request, response);
			return;
		}

		// Get account ID from request parameter, or use the first account
		String accountIdParam = request.getParameter("accountId");
		int selectedAccountId;
		
		if (accountIdParam != null && !accountIdParam.trim().isEmpty()) {
			selectedAccountId = Integer.parseInt(accountIdParam);
		} else {
			selectedAccountId = accounts.get(0).getAccountId();
		}

		List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(selectedAccountId);
		request.setAttribute("transactions", transactions);
		request.setAttribute("accounts", accounts);
		request.setAttribute("selectedAccountId", selectedAccountId);
		
		System.out.println("Found " + transactions.size() + " transactions for account ID: " + selectedAccountId);

		request.getRequestDispatcher("/passbook.jsp").forward(request, response);
	}

	private void showDepositForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute("customer");
		List<Account> accounts = accountDAO.getAccountsByCustomerIdAll(customer.getCustomerId());
		request.setAttribute("accounts", accounts);
		request.getRequestDispatcher("/deposit.jsp").forward(request, response);
	}

	private void showWithdrawForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute("customer");
		List<Account> accounts = accountDAO.getAccountsByCustomerIdAll(customer.getCustomerId());
		request.setAttribute("accounts", accounts);
		request.getRequestDispatcher("/withdraw.jsp").forward(request, response);
	}

	private void showTransferForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute("customer");
		List<Account> accounts = accountDAO.getAccountsByCustomerIdAll(customer.getCustomerId());
		request.setAttribute("accounts", accounts);
		request.getRequestDispatcher("/transfer.jsp").forward(request, response);
	}

}
