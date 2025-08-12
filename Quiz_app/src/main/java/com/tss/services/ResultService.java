package com.tss.services;

import com.tss.dao.ResultDAO;

public class ResultService {
	 public static void save(int userId, int score) throws Exception {
	        ResultDAO.saveResult(userId, score);
	    }
}
