package com.tss.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tss.DBConnection.DBConnection;
import com.tss.dao.QuestionDAO;

public class QuestionService {

	 public static ResultSet getQuestion(int id) throws Exception {
	        return QuestionDAO.getQuestionById(id);
	    }

	    public static String getCorrectAnswer(int id) throws Exception {
	        return QuestionDAO.getCorrectAnswer(id);
	    }
	    public static int getTotalQuestions() throws Exception {
	        int total = 0;
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM questions");
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            total = rs.getInt(1);
	        }
	        return total;
	    }
}
