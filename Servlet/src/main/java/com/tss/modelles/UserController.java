package com.tss.modelles;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tss.model.User;

import Dbconnection.DBConnection;

/**
 * Servlet implementation class UserController
 */
@WebServlet("/UserController")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public UserController() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 String username = request.getParameter("username");
	     String password = request.getParameter("password");

	        
	        User user = new User(username, password);

	        try (Connection conn = DBConnection.getConnection()) {
	            String sql = "INSERT INTO username (username, password) VALUES (?, ?)";
	            PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setString(1, user.getUsername());
	            stmt.setString(2, user.getPassword());

	            int result = stmt.executeUpdate();

	            response.setContentType("text/html");
	            PrintWriter out = response.getWriter();

	            if (result > 0) {
	                out.println("<h2 style='color:green;'>User registered successfully!</h2>");
	            } else {
	                out.println("<h2 style='color:red;'>Failed to register user.</h2>");
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

}
