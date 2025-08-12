package com.tss.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.services.QuestionService;

@WebServlet("/SubmitAnswerServlet")
public class SubmitAnswerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SubmitAnswerServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int qid = Integer.parseInt(request.getParameter("qid"));
			String answer = request.getParameter("answer");

			HttpSession session = request.getSession();
			@SuppressWarnings("unchecked")
			Map<Integer, String> answers = (Map<Integer, String>) session.getAttribute("answers");

			if (answers != null) {
				answers.put(qid, answer);
			}

			int totalQuestions = QuestionService.getTotalQuestions();

			if (qid < totalQuestions) {
				response.sendRedirect("QuestionServlet?qid=" + (qid + 1));
			} else {
				response.sendRedirect("ResultServlet");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong.");
		}
	}
}
