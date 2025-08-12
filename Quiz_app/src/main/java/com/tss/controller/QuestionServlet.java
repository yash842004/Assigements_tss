package com.tss.controller;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tss.services.QuestionService;

@WebServlet("/QuestionServlet")
public class QuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public QuestionServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int qid = 1;

		String qParam = request.getParameter("qid");
		if (qParam != null) {
			try {
				qid = Integer.parseInt(qParam);
			} catch (NumberFormatException e) {
				qid = 1;
			}
		}

		try {
			int totalQuestions = QuestionService.getTotalQuestions();

			if (qid > totalQuestions) {
				response.sendRedirect(request.getContextPath() + "/ResultServlet");
				return;
			}

			ResultSet rs = QuestionService.getQuestion(qid);
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			if (rs != null && rs.next()) {
				String question = rs.getString("question_text");
				String a = rs.getString("option_a");
				String b = rs.getString("option_b");
				String c = rs.getString("option_c");
				String d = rs.getString("option_d");

				out.println("<html><head><title>Question</title><style>");
				out.println("body { font-family: Arial; background-color: #f0f0f0; }");
				out.println(
						".question-box { background: #fff; padding: 20px; margin: 50px auto; width: 50%; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }");
				out.println(".option { margin: 10px 0; }");
				out.println("</style></head><body>");
				out.println("<div class='question-box'>");
				out.println("<h3>Question " + qid + ":</h3>");
				out.println("<p>" + question + "</p>");
				out.println("<form action='SubmitAnswerServlet' method='post'>");
				out.println("<input type='hidden' name='qid' value='" + qid + "'/>");
				out.println(
						"<div class='option'><input type='radio' name='answer' value='a' required> " + a + "</div>");
				out.println("<div class='option'><input type='radio' name='answer' value='b'> " + b + "</div>");
				out.println("<div class='option'><input type='radio' name='answer' value='c'> " + c + "</div>");
				out.println("<div class='option'><input type='radio' name='answer' value='d'> " + d + "</div>");
				out.println("<br/><input type='submit' value='Next'/>");
				out.println("</form></div></body></html>");
			} else {
				response.sendRedirect(request.getContextPath() + "/ResultServlet");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
