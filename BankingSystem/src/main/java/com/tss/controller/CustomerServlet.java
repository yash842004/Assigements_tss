package com.tss.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.model.Account;
import com.tss.model.Customer;
import com.tss.model.Transaction;
import com.tss.service.AccountService;
import com.tss.service.TransactionService;

@WebServlet("/customer/*")
public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AccountService accountService = new AccountService();
	private TransactionService transactionService = new TransactionService();

	public CustomerServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			resp.sendRedirect("/login");
			return;
		}

		Customer customer = (Customer) session.getAttribute("user");
		String path = req.getPathInfo();

		if (path == null || "/dashboard".equals(path)) {
			req.getRequestDispatcher("/WEB-INF/jsp/customer/dashboard.jsp").forward(req, resp);

		} else if ("/account".equals(path)) {
			Account account = accountService.getAccountByCustomerId(customer.getId());
			req.setAttribute("account", account);
			req.getRequestDispatcher("/WEB-INF/jsp/customer/account.jsp").forward(req, resp);

		} else if ("/passbook".equals(path)) {
			Account account = accountService.getAccountByCustomerId(customer.getId());
			// ✅ Pass accountId (int), not accountNumber (String)
			List<Transaction> transactions = transactionService.getPassbook(account.getId());
			req.setAttribute("transactions", transactions);
			req.getRequestDispatcher("/WEB-INF/jsp/customer/passbook.jsp").forward(req, resp);

		} else if ("/transaction".equals(path)) {
			req.getRequestDispatcher("/WEB-INF/jsp/customer/transaction.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			resp.sendRedirect("/login");
			return;
		}

		Customer customer = (Customer) session.getAttribute("user");
		Account account = accountService.getAccountByCustomerId(customer.getId());
		String path = req.getPathInfo();

		if ("/transaction".equals(path)) {
			String type = req.getParameter("type");
			double amount = Double.parseDouble(req.getParameter("amount"));
			String description = req.getParameter("description");
			String toAccountNumber = req.getParameter("toAccountNumber");

			try {
				if ("deposit".equals(type)) {
					transactionService.deposit(account.getId(), amount, description);

				} else if ("withdraw".equals(type)) {
					transactionService.withdraw(account.getId(), amount, description);

				} else if ("transfer".equals(type)) {
					// ✅ Use getByAccountNumber(String) instead of getAccountById(int)
					Account toAccount = accountService.getByAccountNumber(toAccountNumber);

					if (toAccount != null) {
						transactionService.transfer(account.getId(), toAccount.getId(), amount, description);
					} else {
						req.setAttribute("error", "Invalid target account");
						req.getRequestDispatcher("/WEB-INF/jsp/customer/transaction.jsp").forward(req, resp);
						return;
					}
				}

				resp.sendRedirect("/customer/passbook");

			} catch (Exception e) {
				req.setAttribute("error", e.getMessage());
				req.getRequestDispatcher("/WEB-INF/jsp/customer/transaction.jsp").forward(req, resp);
			}
		}
	}
}
