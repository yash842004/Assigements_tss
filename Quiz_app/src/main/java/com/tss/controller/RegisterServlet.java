package com.tss.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tss.services.UserService;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RegisterServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		  String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        String email = request.getParameter("email");
	        try {
	            if (UserService.register(username, password, email)) {
	            	response.sendRedirect("login.html");
	            } else {
	            	response.getWriter().print("Registration failed");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	}


