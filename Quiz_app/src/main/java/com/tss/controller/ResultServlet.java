package com.tss.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.services.QuestionService;
import com.tss.services.ResultService;

@WebServlet("/ResultServlet")
public class ResultServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ResultServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
			response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        @SuppressWarnings("unchecked")
        Map<Integer, String> answers = (Map<Integer, String>) session.getAttribute("answers");

        int score = 0;
        try {
            for (Map.Entry<Integer, String> entry : answers.entrySet()) {
                String correct = QuestionService.getCorrectAnswer(entry.getKey());
                if (correct != null && correct.equalsIgnoreCase(entry.getValue())) {
                    score++;
                }
            }

            ResultService.save(userId, score);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Result</title>");
            out.println("<style>");
            out.println("body {font-family: Arial; background: #fff3e0; padding: 40px;}");
            out.println(".container {max-width: 400px; margin: auto; background: white; padding: 20px; border-radius: 10px; text-align: center;}");
            out.println("h2 {font-size: 24px; color: #333;}");
            out.println("p {font-size: 20px; color: #4CAF50;}");
            out.println("a {margin-top: 20px; display: inline-block; text-decoration: none; color: #fff; background: #f44336; padding: 10px 15px; border-radius: 4px;}");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<div class='container'>");
            out.println("<h2>Your Quiz Result</h2>");
            out.println("<p>Your Score: " + score + "</p>");
            out.println("<a href='logout'>Logout</a>");
            out.println("</div>");
            out.println("</body></html>");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while calculating result.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
