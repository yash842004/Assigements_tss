package com.tss.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.tss.model.Employee;
import com.tss.service.LeaveRequestService;
import com.tss.model.LeaveRequest;

@WebServlet("/employeeDashboard")
public class EmployeeDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LeaveRequestService leaveRequestService = new LeaveRequestService();

    public EmployeeDashboardServlet() {
        super();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("employee") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Employee emp = (Employee) session.getAttribute("employee");
        List<LeaveRequest> requests = leaveRequestService.getAllRequests(); // Adjust to getRequestsByEmpId if needed
        req.setAttribute("requests", requests);
        req.getRequestDispatcher("/employeeDashboard.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}