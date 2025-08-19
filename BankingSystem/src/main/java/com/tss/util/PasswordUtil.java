package com.tss.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtil {
	public static String hash(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashed = md.digest(password.getBytes());
			return Base64.getEncoder().encodeToString(hashed);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Hashing failed", e);
		}
	}

	public static boolean verify(String input, String stored) {
		return hash(input).equals(stored);
	}

}
