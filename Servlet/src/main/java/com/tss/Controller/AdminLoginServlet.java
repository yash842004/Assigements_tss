package com.tss.Controller;

import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/adminLogin")
public class AdminLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public AdminLoginServlet() {
        super();
    }
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		   HttpSession session = request.getSession();
	        String name = (String) session.getAttribute("username");
	        String role = (String) session.getAttribute("role");

	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();

	        if (role != null && role.equalsIgnoreCase("Admin")) {
	            out.println("<h1>Welcome Admin " + name + "</h1>");
	        } else {
	            out.println("<h3>Unauthorized Access</h3>");
	        }}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	


}
