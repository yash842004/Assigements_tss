package com.tss.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.tss.model.Feedback;
import com.tss.service.FeedbackService;

@WebServlet("/submitFeedback")
public class FeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private FeedbackService feedbackService = new FeedbackService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Feedback feedback = new Feedback();
            feedback.setName(request.getParameter("name"));
            feedback.setDate(request.getParameter("date"));
            feedback.setSessionContent(Integer.parseInt(request.getParameter("sessionContent")));
            feedback.setQueryResolution(Integer.parseInt(request.getParameter("queryResolution")));
            feedback.setInteractivity(Integer.parseInt(request.getParameter("interactivity")));
            feedback.setImpactfulLearning(Integer.parseInt(request.getParameter("impactfulLearning")));
            feedback.setContentDelivery(Integer.parseInt(request.getParameter("contentDelivery")));

            feedbackService.submitFeedback(feedback);

            request.setAttribute("feedback", feedback);
            request.getRequestDispatcher("feedback.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("feedback.jsp").forward(request, response);
        }
    }
}
