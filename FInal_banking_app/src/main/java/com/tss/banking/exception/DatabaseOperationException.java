package com.tss.banking.exception;
public class DatabaseOperationException extends BankingException {
    public DatabaseOperationException(String operation, String message) {
        super(String.format("Database %s operation failed: %s", operation, message), "DATABASE_ERROR");
    }
    public DatabaseOperationException(String operation, String message, Throwable cause) {
        super(String.format("Database %s operation failed: %s", operation, message), "DATABASE_ERROR", cause);
    }
    public static DatabaseOperationException saveOperation(String entity, Throwable cause) {
        return new DatabaseOperationException("save", "Failed to save " + entity, cause);
    }
    public static DatabaseOperationException updateOperation(String entity, Throwable cause) {
        return new DatabaseOperationException("update", "Failed to update " + entity, cause);
    }
    public static DatabaseOperationException deleteOperation(String entity, Throwable cause) {
        return new DatabaseOperationException("delete", "Failed to delete " + entity, cause);
    }
    public static DatabaseOperationException connectionFailure() {
        return new DatabaseOperationException("connection", "Database connection failed");
    }
}
