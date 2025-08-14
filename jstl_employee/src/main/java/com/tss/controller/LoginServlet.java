package com.tss.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.tss.service.EmployeeService;
import com.tss.service.AdminService;
import com.tss.model.Employee;
import com.tss.model.Admin;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EmployeeService employeeService = new EmployeeService();
	private AdminService adminService = new AdminService();

	public LoginServlet() {
		super();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("login.jsp").forward(req, resp);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String role = request.getParameter("role");

		if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()
				|| role == null) {
			request.setAttribute("error", "All fields are required.");
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}

		if ("admin".equalsIgnoreCase(role)) {
			System.out.println("Validating admin credentials: " + username + "/" + password);

			Admin admin = adminService.validateAdminLogin(username, password);

			if (admin != null) {
				HttpSession session = request.getSession();
				session.setAttribute("admin", admin);
				response.sendRedirect(request.getContextPath() + "/adminDashboard.jsp");
			} else {
				request.setAttribute("error", "Invalid admin credentials");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}

		} else {
			Employee emp = employeeService.validateEmployeeLogin(username, password);
			if (emp != null) {
				HttpSession session = request.getSession();
				session.setAttribute("employee", emp);
				response.sendRedirect(request.getContextPath() + "/employeeDashboard.jsp");
			} else {
				request.setAttribute("error", "Invalid employee credentials");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		}

	}
}