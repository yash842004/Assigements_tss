package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tss.DBConnection.DBConnection;

public class QuestionDAO {
	
    public static ResultSet getQuestionById(int id) throws Exception {
        Connection connection = DBConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM questions WHERE question_id = ?");
        ps.setInt(1, id);
        return ps.executeQuery();
    }

    public static String getCorrectAnswer(int qid) throws Exception {
        Connection connection = DBConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT correct_option FROM questions WHERE question_id = ?");
        ps.setInt(1, qid);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("correct_option");
        }
        return null;
    }

}
