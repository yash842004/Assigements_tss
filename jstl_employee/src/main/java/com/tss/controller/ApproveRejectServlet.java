// com.tss.controller.ApproveRejectServlet.java
package com.tss.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.tss.service.LeaveRequestService;

@WebServlet("/approveReject")
public class ApproveRejectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LeaveRequestService leaveRequestService = new LeaveRequestService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String leaveIdStr = request.getParameter("leaveId");
        String action = request.getParameter("action");
        String rejectionReason = request.getParameter("rejectionReason");

        if (leaveIdStr == null || action == null) {
            request.setAttribute("error", "Invalid request data");
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
            return;
        }

        try {
            int leaveId = Integer.parseInt(leaveIdStr);
            boolean result = false;
            if ("Approve".equals(action)) {
                result = leaveRequestService.approveLeave(leaveId, -1); // Placeholder empId
            } else if ("Reject".equals(action)) {
                result = leaveRequestService.rejectLeave(leaveId, rejectionReason);
            }

            if (result) {
                response.sendRedirect(request.getContextPath() + "/adminDashboard");
            } else {
                request.setAttribute("error", "Failed to " + action.toLowerCase() + " leave request");
                request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid leave ID");
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
        }
    }
}