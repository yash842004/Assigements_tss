package com.tss.banking.validation.util;
import java.util.regex.Pattern;
public class ValidationPatterns {
    public static final String EMAIL_PATTERN =
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String PHONE_PATTERN =
        "^\\+[1-9]\\d{1,14}$";
    public static final String ACCOUNT_NUMBER_PATTERN =
        "^ACC\\d{10}$";
    public static final String STRONG_PASSWORD_PATTERN =
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
    public static final String NAME_PATTERN =
        "^[a-zA-Z\\s\\-']{2,50}$";
    public static final String NUMERIC_PATTERN =
        "^\\d+$";
    public static final String ALPHANUMERIC_PATTERN =
        "^[a-zA-Z0-9]+$";
    private static final Pattern EMAIL_COMPILED = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern PHONE_COMPILED = Pattern.compile(PHONE_PATTERN);
    private static final Pattern ACCOUNT_NUMBER_COMPILED = Pattern.compile(ACCOUNT_NUMBER_PATTERN);
    private static final Pattern STRONG_PASSWORD_COMPILED = Pattern.compile(STRONG_PASSWORD_PATTERN);
    private static final Pattern NAME_COMPILED = Pattern.compile(NAME_PATTERN);
    private static final Pattern NUMERIC_COMPILED = Pattern.compile(NUMERIC_PATTERN);
    private static final Pattern ALPHANUMERIC_COMPILED = Pattern.compile(ALPHANUMERIC_PATTERN);
    private ValidationPatterns() {
    }
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_COMPILED.matcher(email).matches();
    }
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null) return false;
        String cleaned = phone.replaceAll("[\\s\\-\\(\\)]", "");
        return PHONE_COMPILED.matcher(cleaned).matches();
    }
    public static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && ACCOUNT_NUMBER_COMPILED.matcher(accountNumber).matches();
    }
    public static boolean isStrongPassword(String password) {
        return password != null && STRONG_PASSWORD_COMPILED.matcher(password).matches();
    }
    public static boolean isValidName(String name) {
        return name != null && NAME_COMPILED.matcher(name.trim()).matches();
    }
    public static boolean isNumeric(String str) {
        return str != null && NUMERIC_COMPILED.matcher(str).matches();
    }
    public static boolean isAlphanumeric(String str) {
        return str != null && ALPHANUMERIC_COMPILED.matcher(str).matches();
    }
    public static String generateAccountNumber() {
        StringBuilder sb = new StringBuilder("ACC");
        for (int i = 0; i < 10; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }
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
    public static String maskPhoneNumber(String phone) {
        if (phone == null || phone.length() < 4) {
            return phone;
        }
        return "***" + phone.substring(phone.length() - 4);
    }
    public static String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 7) {
            return accountNumber;
        }
        return accountNumber.substring(0, 3) + "****" + accountNumber.substring(accountNumber.length() - 3);
    }
}
