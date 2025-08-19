package com.tss.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.model.Customer;
import com.tss.service.CustomerService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CustomerService customerService = new CustomerService();

	public LoginServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		Customer customer = customerService.login(username, password);
		if (customer != null) {
			HttpSession session = req.getSession();
			session.setAttribute("user", customer);
			session.setAttribute("role", customer.getRole());
			if ("admin".equals(customer.getRole())) {
				resp.sendRedirect("/admin/dashboard");
			} else {
				resp.sendRedirect("/customer/dashboard");
			}
		} else {
			req.setAttribute("error", "Invalid credentials");
			req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
		}
	}

}
