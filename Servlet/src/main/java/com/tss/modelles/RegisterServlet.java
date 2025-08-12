package com.tss.modelles;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public RegisterServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		  response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	        
	        String name = request.getParameter("name");
	        String address = request.getParameter("address");
	        String gender = request.getParameter("gender");
	        String city = request.getParameter("city");
	        String[] languages = request.getParameterValues("languages");
	        String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        String confirmPassword = request.getParameter("confirmPassword");

	        out.println("<html><body>");
	        out.println("<h2>Registration Details</h2>");
	        out.println("Name: " + name + "<br>");
	        out.println("Address: " + address + "<br>");
	        out.println("Gender: " + gender + "<br>");
	        out.println("City: " + city + "<br>");
	        out.print("Languages: ");
	        if (languages != null) {
	            for (String language : languages) {
	                out.print(language + " ");
	            }
	        } else {
	            out.print("None");
	        }
	        out.println("<br>Username: " + username + "<br>");
	        
	        if (password.equals(confirmPassword)) {
	            out.println("Password Matched!<br>");
	        } else {
	            out.println("Passwords do not match!<br>");
	        }

	        out.println("</body></html>");
	    
	}

}
