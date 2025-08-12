package com.tss.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tss.dao.UserDAO;
import com.tss.model.User;


@WebServlet("/ShowUsersController")
public class ShowUsersController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;
       
   
    public ShowUsersController() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		List<User> users = userDAO.getAllUsers();

      
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
