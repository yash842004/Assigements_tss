package com.tss.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LogoutServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Logout</title>");
        out.println("<style>");
        out.println("body { font-family: Arial; text-align: center; padding-top: 50px; background-color: #f0f0f0; }");
        out.println(".box { display: inline-block; padding: 30px; background: white; border-radius: 10px; box-shadow: 0px 0px 10px rgba(0,0,0,0.1); }");
        out.println("a { margin-top: 20px; display: block; text-decoration: none; color: #fff; background: #4CAF50; padding: 10px 15px; border-radius: 4px; }");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<div class='box'>");
        out.println("<h2>You have been logged out!</h2>");
        out.println("<a href='LoginServlet'>Login Again</a>");
        out.println("</div>");
        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
