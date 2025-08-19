package com.tss.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.tss.model.Employee;
import com.tss.model.LeaveRequest;
import com.tss.service.LeaveRequestService;

@WebServlet("/applyLeave")
public class ApplyLeaveServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LeaveRequestService leaveRequestService = new LeaveRequestService();

    public ApplyLeaveServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employee") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Employee emp = (Employee) session.getAttribute("employee");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String reason = request.getParameter("reason");

        if (startDateStr == null || endDateStr == null || reason == null || reason.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/employeeDashboard.jsp").forward(request, response);
            return;
        }

        try {
            LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1; 
            if (startDate.isAfter(endDate)) {
                request.setAttribute("error", "Start date must be before or equal to end date.");
                request.getRequestDispatcher("/employeeDashboard.jsp").forward(request, response);
                return;
            }
            if (daysBetween > 3) {
                request.setAttribute("error", "Leave request cannot exceed 3 days.");
                request.getRequestDispatcher("/employeeDashboard.jsp").forward(request, response);
                return;
            }

            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.setEmpId(emp.getEmpId());
            leaveRequest.setStartDate(startDateStr);
            leaveRequest.setEndDate(endDateStr);
            leaveRequest.setReason(reason);
            leaveRequest.setStatus("Pending");
            leaveRequest.setRequestDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

            long activeRequests = leaveRequestService.countActiveRequests(emp.getEmpId());
            if (activeRequests >= 3) {
                request.setAttribute("error", "You can have a maximum of 3 active leave requests.");
                request.getRequestDispatcher("/employeeDashboard.jsp").forward(request, response);
                return;
            }

            boolean result = leaveRequestService.applyLeave(leaveRequest);
            if (result) {
                request.setAttribute("message", "Leave request submitted successfully.");
            } else {
                request.setAttribute("error", "Failed to submit leave request.");
            }
            request.getRequestDispatcher("/employeeDashboard.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Invalid date format or server error.");
            request.getRequestDispatcher("/employeeDashboard.jsp").forward(request, response);
        }
    }
}