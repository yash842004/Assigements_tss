package com.tss.Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/FirstController")
public class FirstController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public FirstController() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String firstName = request.getParameter("firstName");

	       
	        HttpSession session = request.getSession();
	        session.setAttribute("firstName", firstName);

	      
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();

	        out.println("<html><body>");
	        out.println("<form action='LastController' method='post'>");
	        out.println("<label>Last Name:</label>");
	        out.println("<input type='text' name='lastName' required />");
	        out.println("<input type='submit' value='OK' />");
	        out.println("</form>");
	        out.println("</body></html>");
	    }

}
