package com.tss.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.tss.services.UserService;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html><head><title>Login</title>");
		out.println("<style>");
		out.println("body { font-family: Arial; background: #f2f2f2; padding: 40px; }");
		out.println(".container { max-width: 400px; margin: auto; background: white; padding: 20px; border-radius: 10px; }");
		out.println("h2 { text-align: center; }");
		out.println("input[type=text], input[type=password] { width: 100%; padding: 10px; margin: 8px 0; border: 1px solid #ccc; border-radius: 4px; }");
		out.println("button { background-color: #4CAF50; color: white; padding: 10px 15px; border: none; border-radius: 4px; width: 100%; }");
		out.println("a { display: block; text-align: center; margin-top: 10px; }");
		out.println("</style>");
		out.println("</head><body>");

		out.println("<div class='container'>");
		out.println("<h2>Login</h2>");
		
		String error = request.getParameter("error");
		if (error != null) {
			out.println("<p style='color:red;text-align:center;'>Invalid Credentials</p>");
		}

		out.println("<form action='LoginServlet' method='post'>");
		out.println("<input type='text' name='username' placeholder='Username' required />");
		out.println("<input type='password' name='password' placeholder='Password' required />");
		out.println("<button type='submit'>Login</button>");
		out.println("</form>");
		out.println("<a href='register.html'>Don't have an account? Register</a>");
		out.println("</div>");

		out.println("</body></html>");
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		try {
			int userId = UserService.login(username, password);
			if (userId != -1) {
				HttpSession session = request.getSession();
				session.setAttribute("userId", userId);
				session.setAttribute("answers", new java.util.HashMap<Integer, String>());
				response.sendRedirect(request.getContextPath() + "/QuestionServlet?qid=1");
			} else {
				response.sendRedirect("LoginServlet?error=1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("LoginServlet?error=1");
		}
	}
}
