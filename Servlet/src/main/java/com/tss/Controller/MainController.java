package com.tss.Controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MainController
 */
@WebServlet("/MainController")
public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public MainController() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String role = request.getParameter("role"); 

	        if ("admin".equalsIgnoreCase(role)) {
	            response.sendRedirect("admin");
	        } else if ("customer".equalsIgnoreCase(role)) {
	            response.sendRedirect("customer");
	        } else {
	            response.getWriter().println("Invalid role. Please select Admin or Customer.");
	        }
	    
	}

}
