package com.tss.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.db.DBConnection;


@WebServlet("/mainLogin")
public class MainLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public MainLoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        String selectedRole = request.getParameter("role");

	        
	        request.getSession().setAttribute("username", username);
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();

	        try (Connection con = DBConnection.getConnection()) {
	            String sql = "SELECT * FROM user WHERE username=? AND password=?";
	            PreparedStatement ps = con.prepareStatement(sql);
	            ps.setString(1, username);
	            ps.setString(2, password);

	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                String dbRole = rs.getString("role");

	                if (!dbRole.equalsIgnoreCase(selectedRole)) {
	                    out.println("<h3 style='color:red;'>Role mismatch! Please select the correct role.</h3>");
	                    out.println("<a href='login.html'>Back to Login</a>");
	                } else {
	                    HttpSession session = request.getSession();
	                    session.setAttribute("username", username);
	                    session.setAttribute("role", dbRole);

	                    if (dbRole.equalsIgnoreCase("Admin")) {
	                        response.sendRedirect("adminLogin");
	                    } else {
	                        response.sendRedirect("customerLogin");
	                    }
	                }
	            } else {
	                out.println("<h3 style='color:red;'>Invalid Username or Password</h3>");
	                out.println("<a href='login.html'>Back to Login</a>");
	            }
	        } catch (Exception e) {
	            e.printStackTrace(out);
	        }
	    }
}
