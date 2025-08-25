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
		try {
			HttpSession session = request.getSession();
			Customer customer = (Customer) session.getAttribute("customer");
			
			String amountStr = request.getParameter("amount");
			String accountIdStr = request.getParameter("accountId");
			
			
			if (amountStr == null || amountStr.trim().isEmpty() || 
				accountIdStr == null || accountIdStr.trim().isEmpty()) {
				request.setAttribute("errorMessage", "Please enter both account and amount.");
				showDepositForm(request, response);
				return;
			}
			
			BigDecimal amount;
			int accountId;
			
			try {
				amount = new BigDecimal(amountStr);
				accountId = Integer.parseInt(accountIdStr);
			} catch (NumberFormatException e) {
				request.setAttribute("errorMessage", "Please enter valid numbers for amount and account.");
				showDepositForm(request, response);
				return;
			}
			
			
			if (amount.compareTo(BigDecimal.ZERO) <= 0) {
				request.setAttribute("errorMessage", "Deposit amount must be greater than zero.");
				showDepositForm(request, response);
				return;
			}

			
			if (amount.compareTo(new BigDecimal("1000000")) > 0) {
				request.setAttribute("errorMessage", "Deposit amount cannot exceed $1,000,000.");
				showDepositForm(request, response);
				return;
			}

			Account account = accountDAO.getAccountById(accountId);
			if (account == null || account.getCustomerId() != customer.getCustomerId()) {
				request.setAttribute("errorMessage", "Invalid account selection.");
				showDepositForm(request, response);
				return;
			}
			
			
			boolean success = accountService.deposit(accountId, amount);

			if (success) {
				System.out.println("TransactionController: Deposit successful");
				request.setAttribute("successMessage", "Deposit of $" + amount + " successful! Your account has been updated.");
				response.sendRedirect("dashboard");
			} else {
				request.setAttribute("errorMessage", "Deposit failed. Please try again or contact support.");
				showDepositForm(request, response);
			}
		} catch (Exception e) {
			System.err.println("TransactionController: Error during deposit: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred during deposit. Please try again.");
			showDepositForm(request, response);
		}
	}

	private void handleWithdraw(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			HttpSession session = request.getSession();
			Customer customer = (Customer) session.getAttribute("customer");
			
			String amountStr = request.getParameter("amount");
			String accountIdStr = request.getParameter("accountId");
			
			
			if (amountStr == null || amountStr.trim().isEmpty() || 
				accountIdStr == null || accountIdStr.trim().isEmpty()) {
				request.setAttribute("errorMessage", "Please enter both account and amount.");
				showWithdrawForm(request, response);
				return;
			}
			
			BigDecimal amount;
			int accountId;
			
			try {
				amount = new BigDecimal(amountStr);
				accountId = Integer.parseInt(accountIdStr);
			} catch (NumberFormatException e) {
				request.setAttribute("errorMessage", "Please enter valid numbers for amount and account.");
				showWithdrawForm(request, response);
				return;
			}
			
		
			if (amount.compareTo(BigDecimal.ZERO) <= 0) {
				request.setAttribute("errorMessage", "Withdrawal amount must be greater than zero.");
				showWithdrawForm(request, response);
				return;
			}

			
			if (amount.compareTo(new BigDecimal("50000")) > 0) {
				request.setAttribute("errorMessage", "Withdrawal amount cannot exceed $50,000 per transaction.");
				showWithdrawForm(request, response);
				return;
			}

			Account account = accountDAO.getAccountById(accountId);
			if (account == null || account.getCustomerId() != customer.getCustomerId()) {
				request.setAttribute("errorMessage", "Invalid account selection.");
				showWithdrawForm(request, response);
				return;
			}
			
			
			if (account.getBalance().compareTo(amount) < 0) {
				request.setAttribute("errorMessage", "Insufficient funds. Available balance: $" + account.getBalance());
				showWithdrawForm(request, response);
				return;
			}
						
			boolean success = accountService.withdraw(accountId, amount);

			if (success) {
				request.setAttribute("successMessage", "Withdrawal of $" + amount + " successful! Your account has been updated.");
				response.sendRedirect("dashboard");
			} else {
				System.out.println("TransactionController: Withdrawal failed");
				request.setAttribute("errorMessage", "Withdrawal failed. Please check your balance and try again.");
				showWithdrawForm(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred during withdrawal. Please try again.");
			showWithdrawForm(request, response);
		}
	}

	private void handleTransfer(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			HttpSession session = request.getSession();
			Customer customer = (Customer) session.getAttribute("customer");
			
			String toAccountNumber = request.getParameter("toAccountNumber");
			String amountStr = request.getParameter("amount");
			String fromAccountIdStr = request.getParameter("fromAccountId");
			
			
			if (toAccountNumber == null || toAccountNumber.trim().isEmpty() || 
				amountStr == null || amountStr.trim().isEmpty() || 
				fromAccountIdStr == null || fromAccountIdStr.trim().isEmpty()) {
				request.setAttribute("errorMessage", "Please fill in all fields.");
				showTransferForm(request, response);
				return;
			}
			
			BigDecimal amount;
			int fromAccountId;
			
			try {
				amount = new BigDecimal(amountStr);
				fromAccountId = Integer.parseInt(fromAccountIdStr);
			} catch (NumberFormatException e) {
				request.setAttribute("errorMessage", "Please enter valid numbers for amount and account.");
				showTransferForm(request, response);
				return;
			}
			
			
			if (amount.compareTo(BigDecimal.ZERO) <= 0) {
				request.setAttribute("errorMessage", "Transfer amount must be greater than zero.");
				showTransferForm(request, response);
				return;
			}

			
			if (amount.compareTo(new BigDecimal("100000")) > 0) {
				request.setAttribute("errorMessage", "Transfer amount cannot exceed $100,000 per transaction.");
				showTransferForm(request, response);
				return;
			}

			Account fromAccount = accountDAO.getAccountById(fromAccountId);
			if (fromAccount == null || fromAccount.getCustomerId() != customer.getCustomerId()) {
				request.setAttribute("errorMessage", "Invalid source account selection.");
				showTransferForm(request, response);
				return;
			}
			
			
			if (fromAccount.getBalance().compareTo(amount) < 0) {
				request.setAttribute("errorMessage", "Insufficient funds in source account. Available balance: $" + fromAccount.getBalance());
				showTransferForm(request, response);
				return;
			}
			
		
			Account toAccount = accountDAO.getAccountByNumber(toAccountNumber.trim());
			if (toAccount == null) {
				request.setAttribute("errorMessage", "Destination account not found. Please check the account number.");
				showTransferForm(request, response);
				return;
			}
			
			
			if (fromAccount.getAccountId() == toAccount.getAccountId()) {
				request.setAttribute("errorMessage", "Cannot transfer money to the same account.");
				showTransferForm(request, response);
				return;
			}
			
			
			String message = accountService.transferMoney(fromAccountId, toAccountNumber.trim(), amount);

			if ("Transfer successful!".equals(message)) {
				request.setAttribute("successMessage", "Transfer of $" + amount + " to account " + toAccountNumber + " successful!");
				response.sendRedirect("dashboard");
			} else {
				request.setAttribute("errorMessage", message);
				showTransferForm(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred during transfer. Please try again.");
			showTransferForm(request, response);
		}
	}

	private void showPassbook(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			Customer customer = (Customer) session.getAttribute("customer");

			
			List<Account> accounts = accountDAO.getAccountsByCustomerIdAll(customer.getCustomerId());
			
			if (accounts == null) {
				accounts = Collections.emptyList();
			}
			
			if (accounts.isEmpty()) {
				request.setAttribute("transactions", Collections.emptyList());
				request.setAttribute("accounts", accounts);
				request.setAttribute("message", "No accounts found. Please open an account first.");
				request.getRequestDispatcher("/passbook.jsp").forward(request, response);
				return;
			}

			
			String accountIdParam = request.getParameter("accountId");
			int selectedAccountId;
			
			if (accountIdParam != null && !accountIdParam.trim().isEmpty()) {
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
			} else {
				selectedAccountId = accounts.get(0).getAccountId();
			}

			List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(selectedAccountId);
			if (transactions == null) {
				transactions = Collections.emptyList();
			}
			
			request.setAttribute("transactions", transactions);
			request.setAttribute("accounts", accounts);
			request.setAttribute("selectedAccountId", selectedAccountId);
			

			request.getRequestDispatcher("/passbook.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred while loading the passbook.");
			request.getRequestDispatcher("/passbook.jsp").forward(request, response);
		}
	}

	private void showDepositForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			Customer customer = (Customer) session.getAttribute("customer");
			List<Account> accounts = accountDAO.getAccountsByCustomerIdAll(customer.getCustomerId());
			
			if (accounts == null) {
				accounts = Collections.emptyList();
			}
			
			if (accounts.isEmpty()) {
				request.setAttribute("errorMessage", "No accounts found. Please open an account first.");
				response.sendRedirect("dashboard");
				return;
			}
			
			request.setAttribute("accounts", accounts);
			request.getRequestDispatcher("/deposit.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred while loading the deposit form.");
			response.sendRedirect("dashboard");
		}
	}

	private void showWithdrawForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			Customer customer = (Customer) session.getAttribute("customer");
			List<Account> accounts = accountDAO.getAccountsByCustomerIdAll(customer.getCustomerId());
			
			if (accounts == null) {
				accounts = Collections.emptyList();
			}
			
			if (accounts.isEmpty()) {
				request.setAttribute("errorMessage", "No accounts found. Please open an account first.");
				response.sendRedirect("dashboard");
				return;
			}
			
			request.setAttribute("accounts", accounts);
			request.getRequestDispatcher("/withdraw.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred while loading the withdraw form.");
			response.sendRedirect("dashboard");
		}
	}

	private void showTransferForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			Customer customer = (Customer) session.getAttribute("customer");
			List<Account> accounts = accountDAO.getAccountsByCustomerIdAll(customer.getCustomerId());
			
			if (accounts == null) {
				accounts = Collections.emptyList();
			}
			
			if (accounts.isEmpty()) {
				request.setAttribute("errorMessage", "No accounts found. Please open an account first.");
				response.sendRedirect("dashboard");
				return;
			}
			
			request.setAttribute("accounts", accounts);
			request.getRequestDispatcher("/transfer.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred while loading the transfer form.");
			response.sendRedirect("dashboard");
		}
	}
}