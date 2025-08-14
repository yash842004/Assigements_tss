package com.tss.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.tss.service.LeaveRequestService;
import com.tss.model.LeaveRequest;

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LeaveRequestService leaveRequestService = new LeaveRequestService();

	public AdminDashboardServlet() {
		super();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session == null || session.getAttribute("admin") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		List<LeaveRequest> requests = leaveRequestService.getAllRequests(); 
		req.setAttribute("requests", requests);
		req.getRequestDispatcher("adminDashboard.jsp").forward(req, resp);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}