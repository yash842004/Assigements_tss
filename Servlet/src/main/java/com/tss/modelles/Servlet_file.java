package com.tss.modelles;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Servlet_file")
public class Servlet_file extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public Servlet_file() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.getWriter().append("Hello ").append(" World! ");
		
		response.setContentType("text/html");
		
		PrintWriter writer =  response.getWriter();
		writer.write("<b>Hey everyone !</b>");
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
