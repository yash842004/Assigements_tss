package com.tss.ShowUsersController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.User;


@WebServlet("/ShowUsersController")
public class ShowUsersController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public ShowUsersController() {
    	User userDAO;
        super();
    }

    public void init() {
        users = new UserDAO();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		 List<User> users = users.getAllUsers();

	        // Print to Console in table format
	        System.out.println("-------------------------------------------------");
	        System.out.printf("%-20s | %-20s\n", "Username", "Password");
	        System.out.println("-------------------------------------------------");

	        for (User u : users) {
	            System.out.printf("%-20s | %-20s\n", u.getUsername(), u.getPassword());
	        }

	        System.out.println("-------------------------------------------------");

	      
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	        out.println("<h3>All user data printed in the server console.</h3>");
	    
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
