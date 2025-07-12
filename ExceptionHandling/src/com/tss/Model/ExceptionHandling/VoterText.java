package com.tss.Model.ExceptionHandling;

import com.model.exception.AgeNotValideException;

public class VoterText {

	public static void main(String[] args) {

		try {
			Voter voter1 = new Voter(12, "yash", 21);

			System.out.println(voter1);

			Voter voter2 = new Voter(1, "om", 12);
		} catch (AgeNotValideException e) {
			System.out.println(e.getMessage());
		}

	}

}
