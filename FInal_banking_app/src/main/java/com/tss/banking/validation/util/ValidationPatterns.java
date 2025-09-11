package com.tss.banking.validation.util;

import java.util.regex.Pattern;

/**
 * Utility class for common validation patterns and methods
 */
public class ValidationPatterns {
    
    // Email pattern
    public static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    
    // Phone number pattern (international format)
    public static final String PHONE_PATTERN = 
        "^\\+[1-9]\\d{1,14}$";
    
    // Account number pattern
    public static final String ACCOUNT_NUMBER_PATTERN = 
        "^ACC\\d{10}$";
    
    // Password pattern (at least 8 chars, 1 upper, 1 lower, 1 digit, 1 special)
    public static final String STRONG_PASSWORD_PATTERN = 
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
    
    // Name pattern (letters, spaces, hyphens, apostrophes)
    public static final String NAME_PATTERN = 
        "^[a-zA-Z\\s\\-']{2,50}$";
    
    // Numeric only pattern
    public static final String NUMERIC_PATTERN = 
        "^\\d+$";
    
    // Alphanumeric pattern
    public static final String ALPHANUMERIC_PATTERN = 
        "^[a-zA-Z0-9]+$";
    
    // Compiled patterns for better performance
    private static final Pattern EMAIL_COMPILED = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern PHONE_COMPILED = Pattern.compile(PHONE_PATTERN);
    private static final Pattern ACCOUNT_NUMBER_COMPILED = Pattern.compile(ACCOUNT_NUMBER_PATTERN);
    private static final Pattern STRONG_PASSWORD_COMPILED = Pattern.compile(STRONG_PASSWORD_PATTERN);
    private static final Pattern NAME_COMPILED = Pattern.compile(NAME_PATTERN);
    private static final Pattern NUMERIC_COMPILED = Pattern.compile(NUMERIC_PATTERN);
    private static final Pattern ALPHANUMERIC_COMPILED = Pattern.compile(ALPHANUMERIC_PATTERN);
    
    private ValidationPatterns() {
        // Utility class
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_COMPILED.matcher(email).matches();
    }
    
    /**
     * Validate phone number format
     */
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null) return false;
        // Remove spaces and special characters except +
        String cleaned = phone.replaceAll("[\\s\\-\\(\\)]", "");
        return PHONE_COMPILED.matcher(cleaned).matches();
    }
    
    /**
     * Validate account number format
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && ACCOUNT_NUMBER_COMPILED.matcher(accountNumber).matches();
    }
    
    /**
     * Validate strong password
     */
    public static boolean isStrongPassword(String password) {
        return password != null && STRONG_PASSWORD_COMPILED.matcher(password).matches();
    }
    
    /**
     * Validate name format
     */
    public static boolean isValidName(String name) {
        return name != null && NAME_COMPILED.matcher(name.trim()).matches();
    }
    
    /**
     * Check if string contains only numbers
     */
    public static boolean isNumeric(String str) {
        return str != null && NUMERIC_COMPILED.matcher(str).matches();
    }
    
    /**
     * Check if string contains only alphanumeric characters
     */
    public static boolean isAlphanumeric(String str) {
        return str != null && ALPHANUMERIC_COMPILED.matcher(str).matches();
    }
    
    /**
     * Generate account number
     */
    public static String generateAccountNumber() {
        // Generate 10 random digits
        StringBuilder sb = new StringBuilder("ACC");
        for (int i = 0; i < 10; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }
    
    /**
     * Mask sensitive data (for logging)
     */
    public static String maskEmail(String email) {
        if (email == null || !isValidEmail(email)) {
            return email;
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return email;
        }
        return email.charAt(0) + "***" + email.substring(atIndex);
    }
    
    /**
     * Mask phone number (for logging)
     */
    public static String maskPhoneNumber(String phone) {
        if (phone == null || phone.length() < 4) {
            return phone;
        }
        return "***" + phone.substring(phone.length() - 4);
    }
    
    /**
     * Mask account number (for logging)
     */
    public static String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 7) {
            return accountNumber;
        }
        return accountNumber.substring(0, 3) + "****" + accountNumber.substring(accountNumber.length() - 3);
    }
}
