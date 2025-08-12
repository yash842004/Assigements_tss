package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tss.DBConnection.DbConnection;
import com.tss.model.Feedback;

public class FeedbackDao {

    public void saveFeedback(Feedback feedback) throws Exception {
        String query = "INSERT INTO feedback(name, date, session_content, query_resolution, interactivity, impactful_learning, content_delivery) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, feedback.getName());
            ps.setString(2, feedback.getDate());
            ps.setInt(3, feedback.getSessionContent());
            ps.setInt(4, feedback.getQueryResolution());
            ps.setInt(5, feedback.getInteractivity());
            ps.setInt(6, feedback.getImpactfulLearning());
            ps.setInt(7, feedback.getContentDelivery());

            ps.executeUpdate();
        }
    }
}
