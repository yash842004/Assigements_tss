package com.tss.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.tss.dao.LeaveRequestDao;
import com.tss.model.Employee;
import com.tss.model.LeaveRequest;

@WebServlet("/submitLeave")
public class SubmitLeaveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LeaveRequestDao lrDao = new LeaveRequestDao();

	public SubmitLeaveServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("employee") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		Employee emp = (Employee) session.getAttribute("employee");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String reason = request.getParameter("reason");

		LeaveRequest leaveRequest = new LeaveRequest();
		leaveRequest.setEmpId(emp.getEmpId());
		leaveRequest.setStartDate(startDate);
		leaveRequest.setEndDate(endDate);
		leaveRequest.setReason(reason);
		leaveRequest.setStatus("Pending");
		leaveRequest.setRequestDate(java.time.LocalDate.now().toString());

		lrDao.saveLeaveRequest(leaveRequest);
		response.sendRedirect(request.getContextPath() + "/employeeDashboard");
	}
}