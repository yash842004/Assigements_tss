package com.tss.service;

import com.tss.dao.FeedbackDao;
import com.tss.exception.InvalidFeedbackException;
import com.tss.exception.InvalidRatingException;
import com.tss.model.Feedback;

public class FeedbackService {
    private FeedbackDao feedbackDao = new FeedbackDao();

    public void submitFeedback(Feedback feedback) throws Exception {
        if (feedback.getName() == null || feedback.getName().trim().isEmpty()) {
            throw new InvalidFeedbackException("Name cannot be empty");
        }
        if (!isValidRating(feedback.getSessionContent()) ||
            !isValidRating(feedback.getQueryResolution()) ||
            !isValidRating(feedback.getInteractivity()) ||
            !isValidRating(feedback.getImpactfulLearning()) ||
            !isValidRating(feedback.getContentDelivery())) {
            throw new InvalidRatingException("Ratings must be between 1 and 5");
        }

        feedbackDao.saveFeedback(feedback);
    }

    private boolean isValidRating(int rating) {
        return rating >= 1 && rating <= 5;
    }
}
