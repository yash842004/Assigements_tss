package com.tss.Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/LastController")
public class LastController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public LastController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  String lastName = request.getParameter("lastName");

	       
	        HttpSession session = request.getSession(false); 
	        String firstName = null;

	        if (session != null) {
	            firstName = (String) session.getAttribute("firstName");
	        }

	       
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();

	        out.println("<html><body>");
	        out.println("<h2>User Details:</h2>");
	        if (firstName != null) {
	            out.println("First Name: " + firstName + "<br/>");
	        } else {
	            out.println("First Name: Not Found in Session<br/>");
	        }
	        out.println("Last Name: " + lastName);
	        out.println("</body></html>");
	    
	}

}
