package com.tss.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.model.Customer;
import com.tss.service.CustomerService;
import com.tss.service.TransactionService;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CustomerService customerService = new CustomerService();
	private TransactionService transactionService = new TransactionService();

	public AdminServlet() {
		super();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session == null || !"admin".equals(session.getAttribute("role"))) {
			resp.sendRedirect("/login");
			return;
		}

		String path = req.getPathInfo();

		if (path == null || "/dashboard".equals(path)) {
			req.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(req, resp);
		} else if ("/customers".equals(path)) {
			List<Customer> customers = customerService.getAll();
			req.setAttribute("customers", customers);
			req.getRequestDispatcher("/WEB-INF/jsp/admin/customers.jsp").forward(req, resp);
		} else if ("/reports".equals(path)) {
			// Assume add getTotalTransactions or similar in TransactionService
			int totalTransactions = transactionService.getTotalTransactions(); // Implement this
			req.setAttribute("totalTransactions", totalTransactions);
			req.getRequestDispatcher("/WEB-INF/jsp/admin/reports.jsp").forward(req, resp);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
